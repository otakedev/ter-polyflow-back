package fr.polytech.user.components;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fr.polytech.email.components.EmailSender;
import fr.polytech.email.components.content.ContentBuilder;
import fr.polytech.email.errors.MessageNotSentException;
import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.User;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.repositories.AdministratorRepository;
import fr.polytech.entities.repositories.StudentRepository;
import fr.polytech.entities.repositories.UserRepository;
import fr.polytech.user.utils.RandomPasswordGenerator;

@Component
@ComponentScan("fr.polytech.entities.repositories")
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class UserBean implements UserManager {

    private static final Object LINK_FRONT = "http://localhost:8080";

    private RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSender sender;

    @Autowired
    private ContentBuilder cb;

    @Autowired
    UserRepository ur;

    @Autowired
    AdministratorRepository ar;

    @Autowired
    StudentRepository sr;

    @Override
    public List<User> getUsers() {
        return (List<User>) ur.findAll();
    }

    @Override
    public List<Administrator> getAdministrators() {
        return (List<Administrator>) ar.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return ur.getUserByEmail(email);
    }

    @Override
    public void setWish(Student student, Wish wish) {
        student.setWish(wish);
        sr.save(student);
    }

    @Override
    public Student getStudentById(Long studentID) {
        Optional<Student> student = sr.findById(studentID);
        if (student.isPresent()) {
            return student.get();
        }
        return null;
    }

    @Override
    public Administrator getAdministratorById(Long id) {
        Optional<Administrator> admin = ar.findById(id);
        if (admin.isPresent()) {
            return admin.get();
        }
        return null;
    }

    @Override
    public Administrator createAdmin(String email, String firstname, String lastname, String occupation)
            throws MessageNotSentException {
        Administrator admin = new Administrator();
        String password = randomPasswordGenerator.generateSecureRandomPassword();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFirstname(firstname);
        admin.setLastname(lastname);
        admin.setOccupation(occupation);
        ar.save(admin);
        sender.sendTemplateMessage(email, "Création de compte",
                cb.init("admin").put("username", email).put("password", password).put("link", LINK_FRONT).render());
        return admin;
    }

    @Override
    public Administrator getAdminByEmail(String email) {
        Optional<Administrator> admin = ar.findByEmail(email);
        if (admin.isPresent()) {
            return admin.get();
        }
        return null;
    }
}
