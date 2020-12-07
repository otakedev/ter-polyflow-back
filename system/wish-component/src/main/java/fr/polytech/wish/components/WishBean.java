package fr.polytech.wish.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.course.components.CourseManager;
import fr.polytech.email.components.EmailSender;
import fr.polytech.email.components.content.ContentBuilder;
import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.CourseStudent;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;
import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.models.WishStatus;
import fr.polytech.entities.repositories.WishRepository;
import fr.polytech.user.components.UserManager;
import fr.polytech.wish.errors.WishIsNotValidException;
import fr.polytech.wish.errors.WishNotFoundException;

@Component
@ComponentScan({"fr.polytech.entities.repositories", "fr.polytech.email.components", "fr.polytech.user.components", "fr.polytech.course.components"})
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class WishBean implements WishManager {

    private static final String WISHLINK = "http://localhost:8080/wish/";

    private static final int NB_OPTION_COURSE = 5;
    // private static final int NB_OPTION_COURSE_SANDWICH_COURSE = 4;

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
        return wr.getWishByUuid(uuid);
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
                cb.init("wishes")
                    .put("name", student.getFirstname() + " " + student.getLastname())
                    .put("link", WISHLINK + wish.getUuid())
                    .render());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        um.setWish(student, wish);
        return wish;
    }

    /**
     * Algorithm to verify wishes
     */

    private boolean courseCanBeAdded(Wish wish, Course course) {
        List<MinorCourse> minorsCourses;
        if(wish.getMinor() != null) minorsCourses = cm.getCoursesWithMinor(wish.getMinor());
        else minorsCourses = new ArrayList<>();
        List<Course> optionCourses = wish.getCourses().stream().map(CourseStudent::getCourse).collect(Collectors.toList());
        List<Course> courses = new ArrayList<>(minorsCourses);
        courses.addAll(optionCourses);
        return cm.courseAreCompatibleWithList(courses, course);
    }

    private boolean coursesCanBeAdded(Wish wish, List<Course> courses) {
        for(Course course : courses) {
            if(!courseCanBeAdded(wish, course)) return false;
        }
        return true;
    }

    private boolean capacityToAddCourse(Wish wish, int count) {
        //TODO add sandwitch course
        return wish.getCourses().size() + count <= NB_OPTION_COURSE;
    }

    private void removeMinorCourses(Wish wish, Minor minor) {
        wish.setCourses(wish.getCourses().stream().filter(e -> e.getCourse().getMinor() != minor).collect(Collectors.toList()));
    }

    private boolean allDependenciesPresent(List<Course> courses) {
        for(Course course : courses) {
            for(Course contraint : course.getContraints()) {
                if(!courses.contains(contraint)) return false;
            }
        }
        return true;
    }

    private boolean allCourseAreNotWishMinor(Wish wish, List<Course> courses) {
        if(wish.getMinor() == null) return true;
        for(Course course : courses) {
            if(course.getMinor() == wish.getMinor()) return false;
        }
        return true;
    }

    @Override
    public Wish putWishCourses(String uuid, List<Course> courses) throws WishNotFoundException,
            WishIsNotValidException {
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
        if(minor != null) {
            removeMinorCourses(wish, minor);
            List<Course> optionCourses = wish.getCourses().stream().map(CourseStudent::getCourse).collect(Collectors.toList());
            for(Course course : cm.getCoursesWithMinor(minor)) {
                if(!cm.courseAreCompatibleWithList(optionCourses, course)) throw new WishIsNotValidException();
            }
        }
        wish.setMinor(minor);
        wr.save(wish);
    }

    @Override
    public void putWishCourses(Wish wish, List<Course> courses) throws WishIsNotValidException {
        /**
         *  Verify there are not too courses
         */
        if(!capacityToAddCourse(wish, courses.size())) throw new WishIsNotValidException();

        /**
         *  Verify there a not minor course following by the student
         */
        if(!allCourseAreNotWishMinor(wish, courses)) throw new WishIsNotValidException();

        List<Course> total = new ArrayList<>(courses);
        List<Course> optionCourses = wish.getCourses().stream().map(CourseStudent::getCourse).collect(Collectors.toList());
        total.addAll(optionCourses);

        /**
         * Verify there are all dependencies in courses
         */
        if(!allDependenciesPresent(total)) throw new WishIsNotValidException();

        /**
         * Verify there are not conflit between courses
         */
        if(!coursesCanBeAdded(wish, courses)) throw new WishIsNotValidException();
        
        wish.getCourses().addAll(courses.stream().map(e -> cm.createCourseStudent(e)).collect(Collectors.toList()));
        wr.save(wish);
    }

    @Override
    public void removeWishCourses(Wish wish, List<Course> courses) {
        wish.setCourses(wish.getCourses().stream().filter(e -> courses.contains(e.getCourse())).collect(Collectors.toList()));
        wr.save(wish);
    }

    @Override
    public Wish removeWishCourses(String uuid, List<Course> courses) throws WishNotFoundException {
        Wish wish = getWishFromUuid(uuid);
        removeWishCourses(wish, courses);
        return wish;
    }
}
