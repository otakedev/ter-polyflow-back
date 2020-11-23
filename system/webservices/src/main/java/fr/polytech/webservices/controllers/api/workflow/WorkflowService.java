package fr.polytech.webservices.controllers.api.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.webservices.errors.BadRequestException;
import fr.polytech.webservices.errors.ResourceNotFoundException;
import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflowmanager.components.WorkflowManager;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowStepNotFound;

import java.util.List;

@RestController
@SpringBootApplication
@RequestMapping("/api/workflow")
@ComponentScan({ "fr.polytech.workflowmanager" })
public class WorkflowService {

    @Autowired
    WorkflowManager wm;
    
    @CrossOrigin
    @GetMapping("")
    public List<Workflow> getWorkflows() {
        return wm.getWorkflows();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Workflow getWorkflow(@PathVariable Long id) {
        try {
            return wm.getWorkflowById(id);
        } catch(WorkflowNotFound e) {
            throw new ResourceNotFoundException();
        }
    }

    @CrossOrigin
    @PutMapping("/{workflowId}/step/{stepId}")
    public Workflow updateCurrentStep(@PathVariable Long workflowId, @PathVariable Long stepId) {
        try {
            return wm.updateCurrentStep(workflowId, stepId);
        } catch(WorkflowNotFound e) {
            throw new ResourceNotFoundException();
        } catch(WorkflowStepNotFound e) {
            throw new BadRequestException();
        } catch(WorkflowHasNotWorkflowStepException e) {
            throw new BadRequestException();
        }
        
    }
}
