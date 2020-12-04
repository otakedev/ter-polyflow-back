package fr.polytech.wish.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.HalfDay;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;
import fr.polytech.entities.models.Period;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.repositories.CourseRepository;
import fr.polytech.entities.repositories.CourseStudentRepository;
import fr.polytech.entities.repositories.MinorCourseRepository;
import fr.polytech.entities.repositories.WishRepository;
import fr.polytech.wish.builder.CourseBuilder;
import fr.polytech.wish.builder.WishBuilder;
import fr.polytech.wish.errors.WishIsNotValidException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WishTest {

	@Autowired
	private CourseBuilder courseBuilder;

	@Autowired
	private WishBuilder wishBuilder;

	@Autowired
	private WishManager wishManager;

	@MockBean
	private CourseRepository cr;

	@MockBean
	private WishRepository wr;

	@MockBean
	private MinorCourseRepository mcr;

	@MockBean
	private CourseStudentRepository csr;

	@Test
	void loaded() {
		assertNotNull(courseBuilder);
		assertNotNull(wishBuilder);
	}

	@Test
	void putWishCourses1() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.render();

		assertDoesNotThrow(() -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test
	void putWishCourses2() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c3", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.render();
		assertThrows(WishIsNotValidException.class, () -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test
	void putWishCourses3() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c4", Period.FIRST_BIMONTHLY, 4, HalfDay.AFTERNOON)
				.addCourse("c5", Period.FIRST_BIMONTHLY, 2, HalfDay.MORNING).contraint("c4", "c5").render();
		assertThrows(WishIsNotValidException.class, () -> {
			wishManager.putWishCourses(wish,
					toAdd.stream().filter(e -> !e.getDescription().equals("c5")).collect(Collectors.toList()));
		});
	}

	@Test
	void putWishCourses6() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c4", Period.FIRST_BIMONTHLY, 4, HalfDay.AFTERNOON)
				.addCourse("c5", Period.FIRST_BIMONTHLY, 2, HalfDay.MORNING).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishCourses(wish,
					toAdd.stream().filter(e -> !e.getDescription().equals("c5")).collect(Collectors.toList()));
		});
	}

	@Test
	void putWishCourses7() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c4", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c5", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c6", Period.FIRST_BIMONTHLY, 4, HalfDay.AFTERNOON)
				.render();
		assertThrows(WishIsNotValidException.class, () -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test
	void putWishCourses8() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c4", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c5", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().render();
		assertDoesNotThrow(() -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test
	void putWishCourses9() {
		List<Course> initial = courseBuilder.initBuilder().render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().render();
		assertDoesNotThrow(() -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test
	void putWishCourses10() {
		List<Course> initial = courseBuilder.initBuilder().render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c4", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c5", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test
	void putWishMinor1() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c3", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.AL);
		});
		assertEquals(3, wish.getCourses().size());
		assertEquals(Minor.AL, wish.getMinor());
	}

	@Test
	void putWishMinor2() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON, Minor.AL)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING, Minor.AL)
				.addCourse("c4", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c5", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.AL);
		});
		assertEquals(3, wish.getCourses().size());
		assertEquals(Minor.AL, wish.getMinor());
	}

	@Test
	void putWishMinor3() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON, Minor.AL)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING, Minor.AL)
				.addCourse("c4", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c5", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.IHM);
		});
		assertEquals(5, wish.getCourses().size());
		assertEquals(Minor.IHM, wish.getMinor());
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.AL);
		});
		assertEquals(3, wish.getCourses().size());
		assertEquals(Minor.AL, wish.getMinor());
	}

	@Test
	void putWishMinor4() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON, Minor.AL)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING, Minor.AL)
				.addCourse("c4", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c5", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.IHM);
		});
		assertEquals(5, wish.getCourses().size());
		assertEquals(Minor.IHM, wish.getMinor());
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, null);
		});
		assertEquals(5, wish.getCourses().size());
		assertNull(wish.getMinor());
	}

	@Test
	void putWishMinorAndputWishCourses1() {
		List<Course> initial = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c1", Period.SECOND_BIMONTHLY, 3, HalfDay.MORNING)
				.addCourse("c2", Period.SECOND_BIMONTHLY, 3, HalfDay.AFTERNOON).render();
		Wish wish = wishBuilder.initBuilder().addCourses(initial).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.AL);
		});
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON, Minor.AL)
				.addCourse("c4", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING).render();
		assertThrows(WishIsNotValidException.class, () -> {
			wishManager.putWishCourses(wish, toAdd);
		});
	}

	@Test 
	void putWishMinorConflictOptions() {
		List<MinorCourse> minorCourses = courseBuilder.initBuilder()
				.addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON, Minor.IAM)
				.addCourse("c2", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON, Minor.IAM)
				.render().stream().map(e -> (MinorCourse)e).collect(Collectors.toList());
		Mockito.when(mcr.findCourseByMinor(Minor.IAM)).thenReturn(minorCourses);

		Wish wish = wishBuilder.initBuilder().addCourses(courseBuilder.initBuilder().render()).render();
		assertDoesNotThrow(() -> {
			wishManager.putWishMinor(wish, Minor.IAM);
		});
		List<Course> toAdd = courseBuilder.initBuilder().addCourse("c1", Period.FIRST_BIMONTHLY, 2, HalfDay.AFTERNOON)
				.addCourse("c3", Period.FIRST_BIMONTHLY, 1, HalfDay.AFTERNOON)
				.addCourse("c4", Period.FIRST_BIMONTHLY, 3, HalfDay.MORNING).render();
		assertThrows(WishIsNotValidException.class, () -> {
			wishManager.putWishCourses(wish, toAdd);
		});

	}

}
