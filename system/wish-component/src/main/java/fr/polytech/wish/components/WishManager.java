package fr.polytech.wish.components;

import java.util.List;

import fr.polytech.entities.models.Wish;

public interface WishManager {
    
    List<Wish> getWishs();

    Wish createWish(Long studentID);
}
