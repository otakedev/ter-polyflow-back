package fr.polytech.workflowmanager.components;

import java.util.List;

import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowStepNotFound;

public interface WorkflowManager {
    
    List<Workflow> getWorkflows();
    Workflow getWorkflowById(Long id) throws WorkflowNotFound;
    Workflow updateCurrentStep(Long workflowId, Long stepId) throws WorkflowNotFound, WorkflowStepNotFound, WorkflowHasNotWorkflowStepException;
}
