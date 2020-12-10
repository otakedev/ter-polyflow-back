package fr.polytech.wish.components;

import java.util.List;

import fr.polytech.course.errors.CourseNotFoundException;
import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.Wish;
import fr.polytech.wish.errors.WishIsNotValidException;
import fr.polytech.wish.errors.WishNotFoundException;

public interface WishManager {
    
    List<Wish> getWishs();

    Wish getWishFromUuid(String uuid) throws WishNotFoundException;

    Wish createWish(Long studentID);

    Wish putWishMinor(String uuid, Minor minor) throws WishNotFoundException, WishIsNotValidException;

    Wish putWishCourses(String uuid, List<Course> courses) throws WishNotFoundException, WishIsNotValidException;

    void putWishMinor(Wish wish, Minor minor) throws WishIsNotValidException;

    void putWishCourses(Wish wish, List<Course> courses) throws WishIsNotValidException;

    void removeWishCourses(Wish wish, List<Course> courses);

    Wish removeWishCourses(String uuid, List<Course> courses) throws WishNotFoundException;

    Wish submitWish(String uuid) throws WishIsNotValidException, WishNotFoundException;

    Wish setCancellableCourse(String uuid, String code) throws WishNotFoundException, CourseNotFoundException, WishIsNotValidException;

    Wish setSandwich(String uuid, boolean value) throws WishNotFoundException;

}
