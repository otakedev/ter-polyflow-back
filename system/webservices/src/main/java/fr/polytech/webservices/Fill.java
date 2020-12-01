package fr.polytech.webservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.File;
import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.Workflow;
import fr.polytech.entities.models.WorkflowDetails;
import fr.polytech.entities.models.WorkflowStatus;
import fr.polytech.entities.models.WorkflowStep;
import fr.polytech.entities.repositories.AdministratorRepository;
import fr.polytech.entities.repositories.FileRepository;
import fr.polytech.entities.repositories.StudentRepository;
import fr.polytech.entities.repositories.WorkflowDetailsRepository;
import fr.polytech.entities.repositories.WorkflowRepository;
import fr.polytech.entities.repositories.WorkflowStepRepository;

@Service
public class Fill {

    @Autowired
    AdministratorRepository aRepository;

    @Autowired
    StudentRepository sRepository;

    @Autowired
    WorkflowRepository wRepository;

    @Autowired
    WorkflowStepRepository wsReposity;

    @Autowired
    WorkflowDetailsRepository wdRepository;

    @Autowired
    FileRepository fRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${env.mode}")
    private String env;
    
    private Faker faker = new Faker();

    private Administrator admin;

    public void generate() {
        if(env.equals("test")) {
            createTestAdmin();
            createSomeWorkflows();
        }
    }

    private void createTestAdmin() {
        Administrator admin = new Administrator();
        admin.setEmail("admin@admin.fr");
        admin.setFirstname("admin");
        admin.setLastname("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setProfilePicUrl(faker.internet().url());
        admin.setOccupation(faker.lorem().word());
        aRepository.save(admin);
        this.admin = admin;
    }

    private void createSomeWorkflows() {
        for(long i=0l; i<10l; i++) {
            Student student = new Student();
            student.setAge(faker.random().nextInt(18, 25));
            student.setPassword(passwordEncoder.encode(faker.lorem().characters(10, 20, true)));
            student.setCurrentYear("SI3");
            student.setEmail(faker.internet().emailAddress());
            student.setFirstname(faker.name().firstName());
            student.setLastname(faker.name().lastName());
            student.setGender(Arrays.asList("F", "M").get(faker.random().nextInt(2)));
            student.setProfilePicUrl(faker.internet().url());
            sRepository.save(student);

            for(long j=0; j<30; j++) {

                List<WorkflowStep> steps = new ArrayList<>();
                for(long k=0; k<10; k++) {
                    WorkflowStep step = new WorkflowStep();
                    step.setTitle(faker.lorem().sentence(5));
                    step.setDescription(String.join(" ", faker.lorem().sentences(3)));
                    step.setExternalLink(faker.internet().url());
                    step.setPersonInCharge(new ArrayList<>());
                    step.setStepIndex((int)k);
                    step.setCheckpointDate(faker.date().future(3, TimeUnit.DAYS));
                    wsReposity.save(step);
                    
                    steps.add(step);
                }

                List<File> files = new ArrayList<>();
                for(long k=0; k<10; k++) {
                    File file = new File();
                    file.setAddedDate(faker.date().past(3, TimeUnit.DAYS));
                    file.setName(faker.lorem().word());
                    file.setLink(faker.internet().url());
                    fRepository.save(file);
                    files.add(file);
                }

                WorkflowDetails wDetails = new WorkflowDetails();
                wDetails.setAttendees(new ArrayList<>());
                wDetails.setDescription(String.join(" ", faker.lorem().sentences(3)));
                wDetails.setSteps(steps);
                wDetails.setFiles(files);
                wdRepository.save(wDetails);

                Workflow workflow = new Workflow();
                workflow.setAuthor(admin);
                workflow.setCreationDate(faker.date().past(4, TimeUnit.DAYS));
                workflow.setDeadlineDate(faker.date().future(4, TimeUnit.DAYS));
                workflow.setSubject(faker.lorem().sentence(5));
                workflow.setTarget(student);
                workflow.setTitle(faker.lorem().sentence(5));
                workflow.setCurrentStep(steps.get(faker.random().nextInt(10)));
                workflow.setDetails(wDetails);
                workflow.setStatus(WorkflowStatus.values()[faker.random().nextInt(WorkflowStatus.values().length)]);
                wRepository.save(workflow);
            }
        }
    }
}
