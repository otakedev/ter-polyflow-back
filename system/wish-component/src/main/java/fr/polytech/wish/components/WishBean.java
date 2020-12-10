package fr.polytech.wish.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.course.components.CourseManager;
import fr.polytech.course.errors.CourseNotFoundException;
import fr.polytech.email.components.EmailSender;
import fr.polytech.email.components.content.ContentBuilder;
import fr.polytech.email.errors.MessageNotSentException;
import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.CourseStudent;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;
import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.models.WishStatus;
import fr.polytech.entities.repositories.WishRepository;
import fr.polytech.user.components.UserManager;
import fr.polytech.wish.errors.WishErrorStatus;
import fr.polytech.wish.errors.WishIsNotValidException;
import fr.polytech.wish.errors.WishNotFoundException;

@Component
@ComponentScan({ "fr.polytech.entities.repositories", "fr.polytech.email.components", "fr.polytech.user.components",
        "fr.polytech.course.components" })
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class WishBean implements WishManager {

    private static final String WISHLINK = "http://localhost:8080/wish/";

    private static final int NB_OPTION_COURSE = 5;

    @Autowired
    WishRepository wr;

    @Autowired
    UserManager um;

    @Autowired
    EmailSender es;

    @Autowired
    ContentBuilder cb;

    @Autowired
    CourseManager cm;

    @Override
    public List<Wish> getWishs() {
        return (List<Wish>) wr.findAll();
    }

    @Override
    public Wish getWishFromUuid(String uuid) throws WishNotFoundException {
        Wish wish = wr.getWishByUuid(uuid);
        if (wish == null)
            throw new WishNotFoundException();
        return wish;
    }

    @Override
    public Wish createWish(Long studentID) {
        UUID uuid = UUID.randomUUID();
        Wish wish = new Wish();
        wish.setCourses(new ArrayList<>());
        wish.setCreationDate(new Date());
        wish.setExpiratioDate(new Date(new Date().getTime() + 1000000000l)); // TODO change it
        wish.setLastSubmitionDate(null);
        wish.setMinor(null);
        wish.setUuid(uuid.toString().replace("-", ""));
        wish.setWishStatus(WishStatus.DRAFT);
        wr.save(wish);
        Student student = um.getStudentById(studentID);
        try {
            es.sendTemplateMessage(student.getEmail(), "Vos voeux sont disponibles",
                    cb.init("wishes").put("name", student.getFirstname() + " " + student.getLastname())
                            .put("link", WISHLINK + wish.getUuid()).render());
        } catch (MessageNotSentException e) {} //Message not sent, but wish is created, we continue.
        um.setWish(student, wish);
        return wish;
    }

    /**
     * Algorithm to verify wishes
     */

    private boolean courseCanBeAdded(List<Course> courses, Course course) {
        return cm.courseAreCompatibleWithList(courses, course);
    }

    private boolean courseCanBeAdded(Wish wish, Course course) {
        List<MinorCourse> minorsCourses;
        if (wish.getMinor() != null)
            minorsCourses = cm.getCoursesWithMinor(wish.getMinor());
        else
            minorsCourses = new ArrayList<>();
        List<Course> optionCourses = wish.getCourses().stream().map(CourseStudent::getCourse)
                .collect(Collectors.toList());
        List<Course> courses = new ArrayList<>(minorsCourses);
        courses.addAll(optionCourses);
        return courseCanBeAdded(courses, course);
    }

    private boolean coursesCanBeAdded(Wish wish, List<Course> courses) {
        for (Course course : courses) {
            if (!courseCanBeAdded(wish, course))
                return false;
        }
        return true;
    }

    private boolean capacityToAddCourse(Wish wish, int count) {
        return wish.getCourses().size() + count <= NB_OPTION_COURSE;
    }

    private void removeMinorCourses(Wish wish, Minor minor) {
        wish.setCourses(
                wish.getCourses().stream().filter(e -> e.getCourse().getMinor() != minor).collect(Collectors.toList()));
    }

    private boolean allDependenciesPresent(List<Course> courses) {
        for (Course course : courses) {
            for (Course constraint : course.getConstraints()) {
                if (!courses.contains(constraint))
                    return false;
            }
        }
        return true;
    }

    private boolean allCourseAreNotWishMinor(Wish wish, List<Course> courses) {
        if (wish.getMinor() == null)
            return true;
        for (Course course : courses) {
            if (course.getMinor() == wish.getMinor())
                return false;
        }
        return true;
    }

    @Override
    public Wish putWishCourses(String uuid, List<Course> courses)
            throws WishNotFoundException, WishIsNotValidException {
        Wish wish = getWishFromUuid(uuid);
        putWishCourses(wish, courses);
        return wish;
    }

    @Override
    public Wish putWishMinor(String uuid, Minor minor) throws WishNotFoundException, WishIsNotValidException {
        Wish wish = getWishFromUuid(uuid);
        putWishMinor(wish, minor);
        return wish;
    }

    @Override
    public void putWishMinor(Wish wish, Minor minor) throws WishIsNotValidException {
        if (minor != null) {
            removeMinorCourses(wish, minor);
            List<Course> optionCourses = wish.getCourses().stream().map(CourseStudent::getCourse)
                    .collect(Collectors.toList());
            for (Course course : cm.getCoursesWithMinor(minor)) {
                if (!cm.courseAreCompatibleWithList(optionCourses, course))
                    throw new WishIsNotValidException(WishErrorStatus.CONFLICT_COURSE,
                            "There are some courses at the same time slot between option courses and minor courses");
            }
        }
        wish.setMinor(minor);
        wr.save(wish);
    }

    @Override
    public void putWishCourses(Wish wish, List<Course> courses) throws WishIsNotValidException {
        if (!wish.getWishStatus().equals(WishStatus.DRAFT))
            throw new WishIsNotValidException(WishErrorStatus.WISH_NOT_EDITABLE,
                    "This wish is submitted, is not editable.");
        /**
         * Verify there are not too courses
         */
        if (!capacityToAddCourse(wish, courses.size()))
            throw new WishIsNotValidException(WishErrorStatus.OPTINAL_COURSE_FULL,
                    "There are too courses. Please remove some of them");

        /**
         * Verify there a not minor course following by the student
         */
        if (!allCourseAreNotWishMinor(wish, courses))
            throw new WishIsNotValidException(WishErrorStatus.COURSE_ALREADY_MANDATORY,
                    "Some courses on option list are already on the course minor");

        List<Course> total = new ArrayList<>(courses);
        List<Course> optionCourses = wish.getCourses().stream().map(CourseStudent::getCourse)
                .collect(Collectors.toList());
        total.addAll(optionCourses);

        /**
         * Verify there are all dependencies in courses
         */
        if (!allDependenciesPresent(total))
            throw new WishIsNotValidException(WishErrorStatus.CONSTRAINT_NOT_RESPECTED,
                    "Missing some dependencies courses");

        /**
         * Verify there are not conflit between courses
         */
        if (!coursesCanBeAdded(wish, courses))
            throw new WishIsNotValidException(WishErrorStatus.CONFLICT_COURSE,
                    "There are some courses at the same time slot");

        wish.getCourses().addAll(courses.stream().map(e -> cm.createCourseStudent(e)).collect(Collectors.toList()));
        wr.save(wish);
    }

    @Override
    public void removeWishCourses(Wish wish, List<Course> courses) {
        wish.setCourses(
                wish.getCourses().stream().filter(e -> courses.contains(e.getCourse())).collect(Collectors.toList()));
        wr.save(wish);
    }

    @Override
    public Wish removeWishCourses(String uuid, List<Course> courses) throws WishNotFoundException {
        Wish wish = getWishFromUuid(uuid);
        removeWishCourses(wish, courses);
        return wish;
    }

    @Override
    public Wish submitWish(String uuid) throws WishIsNotValidException, WishNotFoundException {
        Wish wish = getWishFromUuid(uuid);
        if (!wish.getWishStatus().equals(WishStatus.DRAFT))
            throw new WishIsNotValidException(WishErrorStatus.WISH_NOT_EDITABLE,
                    "This wish is submitted, is not editable.");
        if (wish.getMinor() == null)
            throw new WishIsNotValidException(WishErrorStatus.MINOR_NOT_SPECIFIED, "Minor not specified");
        if (wish.getSandwichCourse() == null)
            throw new WishIsNotValidException(WishErrorStatus.SANDWICH_COURSE_NOT_SPECIFIED,
                    "Sandwich course not specified");
        if (!enoughCourses(wish))
            throw new WishIsNotValidException(WishErrorStatus.NOT_ENOUGH_COURSES, "There are not enough courses");
        List<Course> courses = wish.getCourses().stream().map(e -> e.getCourse()).collect(Collectors.toList());
        if (!allDependenciesPresent(courses))
            throw new WishIsNotValidException(WishErrorStatus.CONSTRAINT_NOT_RESPECTED,
                    "Missing some dependencies courses");
        if (!allCourseAreNotWishMinor(wish, courses))
            throw new WishIsNotValidException(WishErrorStatus.COURSE_ALREADY_MANDATORY,
                    "Some courses on option list are already on the course minor");
        if (!capacityToAddCourse(wish, 0))
            throw new WishIsNotValidException(WishErrorStatus.OPTINAL_COURSE_FULL,
                    "There are too courses. Please remove some of them");
        List<Course> verified = new ArrayList<>();
        for (Course course : courses) {
            if (!courseCanBeAdded(verified, course))
                throw new WishIsNotValidException(WishErrorStatus.CONFLICT_COURSE,
                        "There are some courses at the same time slot");
            verified.add(course);
        }
        wish.setWishStatus(WishStatus.SUBMITTED);
        wr.save(wish);
        return wish;
    }

    private boolean enoughCourses(Wish wish) {
        return wish.getCourses().size() == NB_OPTION_COURSE;
    }

    @Override
    public Wish setSandwich(String uuid, boolean value) throws WishNotFoundException {
        Wish wish = getWishFromUuid(uuid);
        wish.setSandwichCourse(value);
        wr.save(wish);
        return wish;
    }

    @Override
    public Wish setCancellableCourse(String uuid, String code) throws WishNotFoundException, CourseNotFoundException,
            WishIsNotValidException {
        Course course = cm.getCourseByCode(code);
        Wish wish = getWishFromUuid(uuid);
        if(wish.getSandwichCourse() == null || !wish.isSandwichCourse()) throw new WishIsNotValidException(WishErrorStatus.CANCELLABLE_NOT_SANDWICH, "Can not set cancellable course when are not sandwich course");
        if(!containsCode(wish, code)) throw new WishIsNotValidException(WishErrorStatus.CANCELLABLE_NOT_CHOOSEN, "Course not be choosen");
        wish.setCancelableCourse(course);
        wr.save(wish);
        return wish;
    }

    private boolean containsCode(Wish wish, String code) {
        List<String> codes = wish.getCourses().stream().map(e -> e.getCourse().getCode()).collect(Collectors.toList());
        return codes.contains(code);
    }
}
