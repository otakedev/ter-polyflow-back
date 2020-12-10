package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.Wish;

public interface WishRepository extends CrudRepository<Wish, Long> {
    public Wish getWishByUuid(String uuid);
}
