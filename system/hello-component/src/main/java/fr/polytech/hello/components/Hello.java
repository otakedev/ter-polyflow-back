package fr.polytech.hello.components;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.hello.api.HelloAPI;

@Component
@ComponentScan("fr.polytech.task.repositories")
@EntityScan("fr.polytech.task.models")
@EnableJpaRepositories("fr.polytech.task.repositories")
public class Hello implements IHello {

    @Override
    public String hello() {
        return new HelloAPI().getHello();
    }
}