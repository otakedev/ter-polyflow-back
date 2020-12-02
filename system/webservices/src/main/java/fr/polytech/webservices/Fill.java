package fr.polytech.webservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.File;
import fr.polytech.entities.models.HalfDay;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;
import fr.polytech.entities.models.Period;
import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.Workflow;
import fr.polytech.entities.models.WorkflowDetails;
import fr.polytech.entities.models.WorkflowStatus;
import fr.polytech.entities.models.WorkflowStep;
import fr.polytech.entities.repositories.AdministratorRepository;
import fr.polytech.entities.repositories.CourseRepository;
import fr.polytech.entities.repositories.FileRepository;
import fr.polytech.entities.repositories.StudentRepository;
import fr.polytech.entities.repositories.WorkflowDetailsRepository;
import fr.polytech.entities.repositories.WorkflowRepository;
import fr.polytech.entities.repositories.WorkflowStepRepository;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    CourseRepository cRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${env.mode}")
    private String env;

    private Faker faker = new Faker();

    private Administrator admin;

    public void generate() throws FileNotFoundException, IOException, ParseException {
        log.info("Generating some data...");
        if (env.equals("test")) {
            log.info("Mode test, generating test admin...");
            createTestAdmin();
            log.info("Test admin generated");
            log.info("Generating some workflows...");
            createSomeWorkflows();
            log.info("Workflows generated");
        }
        log.info("Generated courses");
        createSomeCourses();
        log.info("Generating data done... Server up !!");
    }

    private Course getCourseFromCode(List<Course> courses, String code) {
        List<Course> filtered = courses.stream().filter(course -> course.getCode().equals(code)).collect(Collectors.toList());
        return filtered.isEmpty() ? null : filtered.get(0);
    }

    private void createSomeCourses() throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
        Object obj = parser.parse(new FileReader(classLoader.getResource("courses/course.json").getFile()));
        JSONArray array = (JSONArray) obj;

        Map<String, JSONArray> map = new HashMap<>();
        List<Course> courses = new ArrayList<>();
        log.info("Creating some courses...");
        for(int i=0; i<array.size(); i++)
        {
            JSONObject element = (JSONObject) array.get(i);
            Course course;
            if(element.get("minor") == null)
                course = new Course();
            else {
                MinorCourse mcourse = new MinorCourse();
                Minor minor;
                switch((String)element.get("minor")) {
                    case "AL":
                        minor = Minor.AL;
                        break;
                    case "IAM":
                        minor = Minor.IAM;
                        break;
                    case "SD":
                        minor = Minor.SD;
                        break;
                    case "CASPAR":
                        minor = Minor.CASPAR;
                        break;
                    case "IHM":
                        minor = Minor.IHM;
                        break;
                    case "WEB":
                        minor = Minor.WEB;
                        break;
                    case "UBINET":
                        minor = Minor.UBINET;
                        break;
                    default:
                        log.error((String)element.get("minor") + " is undefined minor");
                        minor = Minor.WEB; //Don't be pass here
                        break;
                }
                mcourse.setMinor(minor);
                course = mcourse;
            }
            course.setCode((String)element.get("code"));
            if(element.get("dayOfTheWeek") != null)
                course.setDayOfTheWeek(Math.toIntExact((long)element.get("dayOfTheWeek")));
            course.setDescription((String)element.get("description"));
            
            if(element.get("period") != null)
            {
                int value;
                try {
                    value = ((Long)element.get("period")).intValue();
                }
                catch(Exception e) {
                    value = -1;
                }
                switch(value) {
                    case 1:
                        course.setPeriod(Period.FIRST_BIMONTHLY);
                        break;
                    case 2:
                        course.setPeriod(Period.SECOND_BIMONTHLY);
                        break;
                    default:
                        course.setPeriod(Period.OTHER);
                        break;
                }
            }
            else course.setPeriod(Period.OTHER);
            if(element.get("halfDay") != null)
            {
                int value;
                try {
                    value = ((Long)element.get("halfDay")).intValue();
                }
                catch(Exception e) {
                    value = -1;
                }
                switch(value) {
                    case 0:
                        course.setHalfDay(HalfDay.MORNING);
                        break;
                    case 1:
                        course.setHalfDay(HalfDay.AFTERNOON); 
                        break;
                    default:
                        course.setHalfDay(HalfDay.MORNING);
                        break;
                }
            }
            else course.setHalfDay(HalfDay.MORNING);
            map.put(course.getCode(), (JSONArray)element.get("constraints"));
            courses.add(course);
        }
        log.info("Adding constraints on courses...");
        for(Course course : courses) {
            List<Course> constraints = new ArrayList<>();
            for(int i=0; i<map.get(course.getCode()).size(); i++) {
                constraints.add(getCourseFromCode(courses, (String)map.get(course.getCode()).get(i)));
            }
            course.setContraints(constraints);
        }
        
        // try {
        //     System.out.println("before save");
        //     System.out.println(courses.size());
        //     if(courses.size() > 0) System.out.println(courses.get(0).toString());
            
        //     System.out.println("after save");
        // } catch (Exception e) {
        //     System.out.println(e.getMessage());
        // }
        cRepository.saveAll(courses);
        log.info("All courses created and saved");
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
        for (long i = 0l; i < 10l; i++) {
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

            for (long j = 0; j < 30; j++) {
                List<WorkflowStep> steps = new ArrayList<>();
                for (long k = 0; k < 10; k++) {
                    WorkflowStep step = new WorkflowStep();
                    step.setTitle(faker.lorem().sentence(5));
                    step.setDescription(String.join(" ", faker.lorem().sentences(3)));
                    step.setExternalLink(faker.internet().url());
                    step.setPersonInCharge(new ArrayList<>());
                    step.setStepIndex((int) k);
                    step.setCheckpointDate(faker.date().future(3, TimeUnit.DAYS));
                    wsReposity.save(step);

                    steps.add(step);
                }
                List<File> files = new ArrayList<>();
                for (long k = 0; k < 10; k++) {
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
