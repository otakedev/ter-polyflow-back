package fr.polytech.user.components;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
@ComponentScan({ "fr.polytech.entities.repositories", "fr.polytech.email.components" })
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class UserBean implements UserManager {

    public class MultipartFileResource extends ByteArrayResource {

        private String filename;
    
        public MultipartFileResource(MultipartFile multipartFile) throws IOException {
            super(multipartFile.getBytes());
            this.filename = multipartFile.getOriginalFilename();
        }
    
        @Override
        public String getFilename() {
            return this.filename;
        }
    }
    
    @Value("${servers.front.host:localhost}")
    private String frontHost;

    @Value("${servers.front.port:8080}")
    private String frontPort;

    @Value("${servers.student_api.host:localhost}")
    private String studentAPIHost;

    @Value("${servers.student_api.port:4000}")
    private String studentAPIPort;

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
                cb.init("admin").put("username", email).put("password", password).put("link", String.format("http://%s:%s", frontHost, frontPort)).render());
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

    @Override
    public List<Student> upload(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartFileResource(file));
        String serverUrl = String.format("http://%s:%s/student", studentAPIHost, studentAPIPort);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Student[]> response = restTemplate.postForEntity(serverUrl, requestEntity, Student[].class);
        List<Student> students = Arrays.asList(response.getBody());
        sr.saveAll(students);
        return students;
    }
}
