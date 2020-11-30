package fr.polytech.workflowmanager.components;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflow.models.WorkflowStep;
import fr.polytech.workflow.repositories.WorkflowRepository;
import fr.polytech.workflow.repositories.WorkflowStepRepository;
import fr.polytech.workflowmanager.errors.WorkflowFieldNotExist;
import fr.polytech.workflowmanager.errors.WorkflowFieldWithNotValueException;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowPageOrElementPerPageNotSpecify;
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
    public List<Workflow> getWorkflows(String field, String value, Integer page, Integer elementPerPage) throws WorkflowFieldWithNotValueException, WorkflowFieldNotExist,
            WorkflowPageOrElementPerPageNotSpecify {
        if((page != null && elementPerPage == null) || (page == null && elementPerPage != null)) throw new WorkflowPageOrElementPerPageNotSpecify();
        if (field == null && page == null)
            return (List<Workflow>) wr.findAll();
        else if(field == null) return wr.findAll(PageRequest.of(page, elementPerPage));
        try {
            Workflow.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new WorkflowFieldNotExist();
        }
        if(value == null) throw new WorkflowFieldWithNotValueException();
        if(page == null) {
            List<Workflow> workflows = wr.search(field, value);
            return workflows;
        }
        return wr.search(field, value, PageRequest.of(page, elementPerPage));
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

    @Override
    public Workflow update(Workflow workflow, Long id) throws WorkflowNotFound {
        Optional<Workflow> op = wr.findById(id);
        if(!op.isPresent()) throw new WorkflowNotFound();
        
        workflow.setId(id);
        wr.save(workflow);

        return workflow;
    }
    
}
