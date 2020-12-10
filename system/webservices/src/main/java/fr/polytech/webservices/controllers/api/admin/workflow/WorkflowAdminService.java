package fr.polytech.webservices.controllers.api.admin.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.webservices.Application;
import fr.polytech.webservices.errors.BadRequestException;
import fr.polytech.webservices.errors.ResourceNotFoundException;
import fr.polytech.webservices.models.WorkflowBody;
import fr.polytech.webservices.models.WorkflowDetailsBody;
import fr.polytech.webservices.models.mapper.Mapper;
import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.Workflow;
import fr.polytech.entities.models.WorkflowDetails;
import fr.polytech.user.components.UserManager;
import fr.polytech.workflowmanager.components.WorkflowManager;
import fr.polytech.workflowmanager.errors.WorkflowFieldNotExist;
import fr.polytech.workflowmanager.errors.WorkflowFieldWithNotValueException;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowPageOrElementPerPageNotSpecify;
import fr.polytech.workflowmanager.errors.WorkflowStepNotFound;

import java.util.List;

@RestController
@RequestMapping("/api/admin/workflow")
@ComponentScan({ "fr.polytech.workflowmanager", "fr.polytech.usermanager" })
public class WorkflowAdminService {

    @Autowired
    WorkflowManager wm;

    @Autowired
    UserManager um;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @CrossOrigin
    @GetMapping("")
    public List<Workflow> getWorkflows(@RequestParam(required = false) String field,
            @RequestParam(required = false) String value, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer elementPerPage) {
        log.info("GET : /api/workflow");
        try {
            return wm.getWorkflows(field, value, page, elementPerPage);
        } catch (WorkflowFieldWithNotValueException e) {
            log.error("Field specify without value");
            throw new BadRequestException();
        } catch (WorkflowFieldNotExist e) {
            log.error("Field " + field + " doesn't exist");
            throw new BadRequestException();
        } catch (WorkflowPageOrElementPerPageNotSpecify e) {
            log.error("Page specify without elementPerPage or reversed");
            throw new BadRequestException();
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Workflow getWorkflow(@PathVariable Long id) {
        log.info("GET : /api/workflow/" + id);
        try {
            return wm.getWorkflowById(id);
        } catch (WorkflowNotFound e) {
            log.error(String.format("Workflow with id %d do not exist", id));
            throw new ResourceNotFoundException();
        }
    }

    @CrossOrigin
    @PutMapping("/{workflowId}/step/{stepId}")
    public Workflow updateCurrentStep(@PathVariable Long workflowId, @PathVariable Long stepId) {
        log.info(String.format("PUT : /api/workflow/%d/step/%d", workflowId, stepId));
        try {
            return wm.updateCurrentStep(workflowId, stepId);
        } catch (WorkflowNotFound e) {
            log.error(String.format("Workflow with id %d do not exist", workflowId));
            throw new ResourceNotFoundException();
        } catch (WorkflowStepNotFound e) {
            log.error(String.format("WorkflowStep with id %d do not exist", stepId));
            throw new BadRequestException();
        } catch (WorkflowHasNotWorkflowStepException e) {
            log.error(String.format("Workflow with id %d do not contain step with id %d", workflowId, stepId));
            throw new BadRequestException();
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public Workflow update(@RequestBody WorkflowBody workflowBody, @PathVariable Long id) {
        log.info("PUT : /api/workflow/" + id);
        try {
            Workflow workflow = wm.getWorkflowById(id);
            Mapper<WorkflowBody, Workflow> mapper = new Mapper<>(workflowBody);
            try {
                mapper.render(workflow);
                mapper.changeElementById(workflow, "target", (objectId) -> um.getStudentById((Long) objectId));
                mapper.changeElementById(workflow, "author", (objectId) -> um.getAdministratorById((Long) objectId));
                mapper.changeElementById(workflow, "currentStep", (objectId) -> wm.getStepById((Long) objectId));
                return wm.update(workflow, id);
            } catch (Exception e) {
                throw new BadRequestException();
            }
        } catch (WorkflowNotFound e) {
            log.error(String.format("Workflow with id %d do not exist", id));
            throw new ResourceNotFoundException();
        }
    }

    @CrossOrigin
    @PutMapping("/{id}/details")
    public WorkflowDetails updateDetails(@RequestBody WorkflowDetailsBody workflowDetailsBody, @PathVariable Long id) {
        log.info("PUT : /api/workflow/" + id);
        try {
            WorkflowDetails workflowDetails = wm.getWorkflowById(id).getDetails();
            Mapper<WorkflowDetailsBody, WorkflowDetails> mapper = new Mapper<>(workflowDetailsBody);
            try {
                mapper.render(workflowDetails);
                mapper.changeElementsById(workflowDetails, "attendees",
                        (objectId -> um.getAdministratorById((Long) objectId)));
                mapper.changeElementsById(workflowDetails, "steps", (objectId -> wm.getStepById((Long) objectId)));
                return wm.updateDetails(workflowDetails, workflowDetails.getId());
            } catch (Exception e) {
                throw new BadRequestException();
            }
        } catch (WorkflowNotFound e) {
            log.error(String.format("Workflow with id %d do not exist", id));
            throw new ResourceNotFoundException();
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/attendees")
    public Workflow addAttendees(@RequestBody Administrator user, @PathVariable Long id) {
        log.info("POST : /api/workflow/" + id + "/attendees");
        try {
            return wm.addAttendees(user, id);
        } catch (WorkflowNotFound e) {
            log.error(String.format("Workflow with id %d do not exist", id));
            throw new ResourceNotFoundException();
        }
    }
}
