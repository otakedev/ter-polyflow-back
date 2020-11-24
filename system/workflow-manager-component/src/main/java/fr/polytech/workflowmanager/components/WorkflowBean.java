package fr.polytech.workflowmanager.components;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflow.models.WorkflowStep;
import fr.polytech.workflow.repositories.WorkflowRepository;
import fr.polytech.workflow.repositories.WorkflowStepRepository;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowStepNotFound;

@Component
@ComponentScan("fr.polytech.workflow.repositories")
@EntityScan("fr.polytech.workflow.models")
@EnableJpaRepositories("fr.polytech.workflow.repositories")
public class WorkflowBean implements WorkflowManager {

    @Autowired
    WorkflowRepository wr;

    @Autowired
    WorkflowStepRepository wrs;

    @Override
    public List<Workflow> getWorkflows() {
        return (List<Workflow>) wr.findAll();
    }

    @Override
    public Workflow getWorkflowById(Long id) throws WorkflowNotFound {
        Optional<Workflow> op = wr.findById(id);
        if(op.isPresent()) return op.get();
        throw new WorkflowNotFound();
    }

    @Override
    public Workflow updateCurrentStep(Long workflowId, Long stepId) throws WorkflowNotFound, WorkflowStepNotFound, WorkflowHasNotWorkflowStepException {
        Optional<Workflow> op_workflow = wr.findById(workflowId);
        if(!op_workflow.isPresent()) throw new WorkflowNotFound();
        
        Optional<WorkflowStep> op_workflow_step = wrs.findById(stepId);
        if(!op_workflow_step.isPresent()) throw new WorkflowStepNotFound();

        Workflow wf = op_workflow.get();
        WorkflowStep wfs = op_workflow_step.get();

        if(!wf.getDetails().getSteps().contains(wfs)) throw new WorkflowHasNotWorkflowStepException();

        wf.setCurrentStep(wfs);
        wr.save(wf);
        return wf;
    }
    
}
