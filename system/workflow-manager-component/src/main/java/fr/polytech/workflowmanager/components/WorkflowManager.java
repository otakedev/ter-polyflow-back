package fr.polytech.workflowmanager.components;

import java.util.List;

import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflowmanager.errors.WorkflowFieldNotExist;
import fr.polytech.workflowmanager.errors.WorkflowFieldWithNotValueException;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowPageOrElementPerPageNotSpecify;
import fr.polytech.workflowmanager.errors.WorkflowStepNotFound;

public interface WorkflowManager {
    
    List<Workflow> getWorkflows(String field, String value, Integer page, Integer elementPerPage) throws WorkflowFieldWithNotValueException, WorkflowFieldNotExist, WorkflowPageOrElementPerPageNotSpecify;
    Workflow getWorkflowById(Long id) throws WorkflowNotFound;
    Workflow updateCurrentStep(Long workflowId, Long stepId) throws WorkflowNotFound, WorkflowStepNotFound, WorkflowHasNotWorkflowStepException;
	Workflow update(Workflow workflow, Long id) throws WorkflowNotFound;
}
