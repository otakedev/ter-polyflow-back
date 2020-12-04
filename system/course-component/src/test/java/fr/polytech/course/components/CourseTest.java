package fr.polytech.course.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.HalfDay;
import fr.polytech.entities.models.Period;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private CourseManager cm;

    private Course createCourse(Period period, int dayOfTheWeek, HalfDay halfDay) {
        
        Course c1 = new Course();
        c1.setCode(UUID.randomUUID().toString());
        c1.setContraints(new ArrayList<>());
        c1.setDayOfTheWeek(dayOfTheWeek);
        c1.setDescription("Lorem ipsum");
        c1.setHalfDay(halfDay);
        c1.setId((long)new Random().nextInt());
        c1.setPeriod(period);
        return c1;
    }

	@Test
    void checkCourseCompatible1() {
        Course c1 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.MORNING);
        assertFalse(cm.coursesAreCompatible(c1, c2));
    }

    @Test
    void checkCourseCompatible2() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.MORNING);
        assertTrue(cm.coursesAreCompatible(c1, c2));
    }

    @Test
    void checkCourseCompatible3() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.MORNING);
        Course c2 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.coursesAreCompatible(c1, c2));
    }

    @Test
    void checkCourseCompatible4() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.MORNING);
        Course c2 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.coursesAreCompatible(c1, c2));
    }

    @Test
    void checkCourseCompatible5() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.AFTERNOON);
        Course c2 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.coursesAreCompatible(c1, c2));
    }

    @Test
    void checkCourseCompatible6() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.coursesAreCompatible(c1, c2));
    }

    @Test
    void checkCoursesCompatibleWithList1() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON);
        Course c3 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.AFTERNOON);

        Course c4 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.courseAreCompatibleWithList(Arrays.asList(c1, c2, c3), c4));
    }

    @Test
    void checkCoursesCompatibleWithList2() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON);
        Course c3 = createCourse(Period.FIRST_BIMONTHLY, 2, HalfDay.MORNING);

        Course c4 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.courseAreCompatibleWithList(Arrays.asList(c1, c2, c3), c4));
    }

    @Test
    void checkCoursesCompatibleWithList3() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 2, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON);
        Course c3 = createCourse(Period.FIRST_BIMONTHLY, 2, HalfDay.MORNING);

        Course c4 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.courseAreCompatibleWithList(Arrays.asList(c1, c2, c3), c4));
    }

    @Test
    void checkCoursesCompatibleWithList4() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.MORNING);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON);
        Course c3 = createCourse(Period.FIRST_BIMONTHLY, 2, HalfDay.MORNING);

        Course c4 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.courseAreCompatibleWithList(Arrays.asList(c1, c2, c3), c4));
    }

    @Test
    void checkCoursesCompatibleWithList5() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        Course c2 = createCourse(Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON);
        Course c3 = createCourse(Period.FIRST_BIMONTHLY, 2, HalfDay.MORNING);

        Course c4 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertFalse(cm.courseAreCompatibleWithList(Arrays.asList(c1, c2, c3), c4));
    }

    @Test
    void checkCoursesCompatibleWithList6() {
        Course c1 = createCourse(Period.SECOND_BIMONTHLY, 1, HalfDay.AFTERNOON);
        assertTrue(cm.courseAreCompatibleWithList(new ArrayList<>(), c1));
    }
}