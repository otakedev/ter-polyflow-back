package fr.polytech.wish.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.entities.models.Wish;
import fr.polytech.entities.models.WishStatus;
import fr.polytech.entities.repositories.WishRepository;
import fr.polytech.user.components.UserManager;
import fr.polytech.wish.errors.WishNotFoundException;

@Component
@ComponentScan("fr.polytech.entities.repositories")
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class WishBean implements WishManager {

    @Autowired
    WishRepository wr;

    @Autowired
    UserManager um;

    @Override
    public List<Wish> getWishs() {
        return (List<Wish>) wr.findAll();
    }

    public Wish getWishFromUuid(String uuid) throws WishNotFoundException {
       return wr.getWishByUuid(uuid);
    }

    @Override
    public Wish createWish(Long studentID) {
        UUID uuid = UUID.randomUUID();
        Wish wish = new Wish();
        wish.setCourses(new ArrayList<>());
        wish.setCreationDate(new Date());
        wish.setExpiratioDate(new Date(new Date().getTime()+1000000000l)); //TODO change it
        wish.setLastSubmitionDate(null);
        wish.setMinor(null);
        wish.setUuid(uuid.toString().replace("-", ""));
        wish.setWishStatus(WishStatus.DRAFT);
        wr.save(wish);
        um.setWish(studentID, wish);
        return wish;
    }
    
}
