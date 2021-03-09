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
    CourseRepository cRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final String FProfilPic = "https://otakedev.com/share/illustration/undraw_female_avatar_w3jk.png";
    private static final String MProfilPic = "https://otakedev.com/share/illustration/undraw_male_avatar_323b.png";

    @Value("${env.mode}")
    private String env;

    private Faker faker = new Faker();

    private Administrator superadmin;

    private List<Administrator> admins = new ArrayList<>();

    public void generate() throws FileNotFoundException, IOException, ParseException {
        log.info("Generating some data...");
        if (env.equals("test")) {
            log.info("Mode test, generating super admin...");
            createSuperAdmin();
            log.info("super admin generated");
            log.info("Mode test, generating some admin...");
            createSomeAdmin();
            log.info("super some generated");
            log.info("Generating some workflows...");
            createSomeWorkflows();
            log.info("Workflows generated");
        }
        log.info("Generated courses");
        createSomeCourses();
        log.info("Generating data done... Server up !!");
    }

    private Course getCourseFromCode(List<Course> courses, String code) {
        List<Course> filtered = courses.stream().filter(course -> course.getCode().equals(code))
                .collect(Collectors.toList());
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
        for (int i = 0; i < array.size(); i++) {
            JSONObject element = (JSONObject) array.get(i);
            Course course;
            if (element.get("minor") == null)
                course = new Course();
            else {
                MinorCourse mcourse = new MinorCourse();
                Minor minor;
                switch ((String) element.get("minor")) {
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
                    log.error((String) element.get("minor") + " is undefined minor");
                    minor = Minor.WEB; // Don't be pass here
                    break;
                }
                mcourse.setMinor(minor);
                course = mcourse;
            }
            course.setCode((String) element.get("code"));
            if (element.get("dayOfTheWeek") != null)
                course.setDayOfTheWeek(Math.toIntExact((Long) element.get("dayOfTheWeek")));
            course.setDescription((String) element.get("description"));

            if (element.get("period") != null) {
                int value;
                try {
                    value = ((Long) element.get("period")).intValue();
                } catch (Exception e) {
                    value = -1;
                }
                switch (value) {
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
            } else
                course.setPeriod(Period.OTHER);
            if (element.get("halfDay") != null) {
                int value;
                try {
                    value = ((Long) element.get("halfDay")).intValue();
                } catch (Exception e) {
                    value = -1;
                }
                switch (value) {
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
            } else
                course.setHalfDay(HalfDay.MORNING);
            map.put(course.getCode(), (JSONArray) element.get("constraints"));
            courses.add(course);
        }
        log.info("Adding constraints on courses...");
        for (Course course : courses) {
            List<Course> constraints = new ArrayList<>();
            for (int i = 0; i < map.get(course.getCode()).size(); i++) {
                constraints.add(getCourseFromCode(courses, (String) map.get(course.getCode()).get(i)));
            }
            course.setConstraints(constraints);
        }

        // try {
        // System.out.println("before save");
        // System.out.println(courses.size());
        // if(courses.size() > 0) System.out.println(courses.get(0).toString());

        // System.out.println("after save");
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }
        cRepository.saveAll(courses);
        log.info("All courses created and saved");
    }

    private void createSuperAdmin() {
        Administrator admin = new Administrator();
        admin.setEmail("admin@admin.fr");
        admin.setFirstname("admin");
        admin.setLastname("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setProfilePicUrl(faker.internet().url());
        admin.setOccupation(faker.lorem().word());
        aRepository.save(admin);
        this.superadmin = admin;
    }

    private void createSomeAdmin() {
        List<String> occupations = Arrays.asList("Professeur", "Responsable", "Scolarité", "Superviseur",
                "Enseignant Externe");
        for (int i = 0; i < 9; i++) {
            Administrator admin = new Administrator();
            String firstname = faker.name().firstName();
            String lastname = faker.name().lastName();
            admin.setEmail(firstname + "." + lastname + "@etu.univ-cotedazur.fr");
            admin.setFirstname(firstname);
            admin.setLastname(lastname);
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setProfilePicUrl(faker.internet().url());
            admin.setOccupation(occupations.get(faker.random().nextInt(occupations.size())));
            aRepository.save(admin);
            admins.add(admin);
        }
    }

    private void createSomeWorkflows() {
        List<Administrator> attendees = admins.stream().filter(e -> faker.bool().bool()).collect(Collectors.toList());

        List<String> filesName = Arrays.asList("Candidature", "Fiche d'informations", "Bultin semestre 1",
                "Bultin semestre 2", "Bultin semestre 3", "Bultin semestre 4", "Appréciations");

        List<String> origins = Arrays.asList("SI3", "SI4", "SI5", "MAM3", "MAM4", "MAM5", "GE3", "GE4", "GE5", "ELEC3",
                "ELEC4", "ELEC5");

        // @formatter:off
        List<WorkflowStep> basicSteps = new ArrayList<>();
        basicSteps.add(new WorkflowStep()
            .title("Attacher les documents de la candidature au workflow")
            .description("Ajouter les documents liés à la candidature au workflow avant de procéder à l'étape suivante afin de partagé tous les fichiers nécessaires."));
        basicSteps.add(new WorkflowStep()
            .title("Vérifier les documents de la candidature")
            .description("Revoir la candidature et les documents attachés et corriger toutes erreurs éventuelles."));
        basicSteps.add(new WorkflowStep()
            .title("Commentaire du premier membre du jury")
            .description("Suite au Jury, veuillez écrire vos observations."));
        basicSteps.add(new WorkflowStep()
            .title("Commentaire du deuxième membre du jury")
            .description("Suite au Jury, veuillez écrire vos observations."));
        basicSteps.add(new WorkflowStep()
            .title("Commentaire du troisième membre du jury")
            .description("Suite au Jury, veuillez écrire vos observations."));
        basicSteps.add(new WorkflowStep()
            .title("Validation du responsable du jury")
            .description("Vérifier les commentaires des autres Jurys pour procéder à la validation finale"));
        basicSteps.add(new WorkflowStep()
            .title("Finir la saisie des informations de l'étudiant")
            .description("Après validation, entrer les informations de l'étudiant et vérifier le dossier"));
        basicSteps.add(new WorkflowStep()
            .title("Enregistrement de l'étudiant par la scolarité")
            .description("Enregistrer l'étudiant et lui communiquer les prochaines instructions."));
        basicSteps.add(new WorkflowStep()
            .title("Contacter le consulat (optionnel)")
            .description("Si l'étudiant et étranger, il sera nécessaire de contacter le consultat pour les problèmes de visa. Sinon, vous pouvez valider cett étape."));
        // @formatter:on

        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            String firstname = faker.name().firstName();
            String lastname = faker.name().lastName();
            student.setEmail("wmozzinor+" + firstname + "." + lastname + "@gmail.com");
            student.setAge(faker.random().nextInt(18, 25));
            student.setCurrentYear(origins.get(faker.random().nextInt(origins.size())));
            student.setFirstname(firstname);
            student.setLastname(lastname);
            String gender = Arrays.asList("F", "M").get(faker.random().nextInt(2));
            student.setGender(gender);
            student.setProfilePicUrl(gender.equals("F") ? FProfilPic : MProfilPic);
            sRepository.save(student);

            List<WorkflowStep> steps = new ArrayList<>();
            int offsetDate = faker.random().nextInt(3, 10);
            for (int k = 0; k < 9; k++) {
                WorkflowStep step = new WorkflowStep();
                step.setTitle(basicSteps.get(k).getTitle());
                step.setDescription(basicSteps.get(k).getDescription());
                step.setExternalLink(faker.internet().url());
                step.setPersonInCharge(admins.get(k));
                step.setStepIndex(k);
                step.setCheckpointDate(faker.date().future(offsetDate + k, TimeUnit.DAYS));
                wsReposity.save(step);

                steps.add(step);
            }
            List<File> files = new ArrayList<>();
            for (int k = 0; k < 7; k++) {
                File file = new File();
                file.setAddedDate(faker.date().past(3, TimeUnit.DAYS));
                file.setName(filesName.get(k));
                file.setLink(faker.internet().url());
                fRepository.save(file);
                files.add(file);
            }

            WorkflowDetails wDetails = new WorkflowDetails();
            wDetails.setAttendees(attendees);
            wDetails.setDescription("Vérification de la Candidature en SI5");
            wDetails.setSteps(steps);
            wDetails.setFiles(files);
            wdRepository.save(wDetails);

            Workflow workflow = new Workflow();
            workflow.setAuthor(superadmin);
            workflow.setCreationDate(faker.date().past(3, TimeUnit.DAYS));
            workflow.setDeadlineDate(steps.get(steps.size() - 1).getCheckpointDate());
            if (faker.random().nextBoolean()) {
                workflow.setSubject(student.getCurrentYear() + " : Redoublement");
            } else {
                workflow.setSubject(student.getCurrentYear() + " : Inscription");
            }
            workflow.setTarget(student);
            workflow.setTitle("DETAILS DU DOSSIER DE CANDIDATURE");
            workflow.setCurrentStep(steps.get(faker.random().nextInt(8)));
            workflow.setDetails(wDetails);
            workflow.setStatus(WorkflowStatus.values()[faker.random().nextInt(WorkflowStatus.values().length)]);
            wRepository.save(workflow);
        }
    }
}
