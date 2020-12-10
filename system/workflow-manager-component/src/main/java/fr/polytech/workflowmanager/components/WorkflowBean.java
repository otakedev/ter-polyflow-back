package fr.polytech.workflowmanager.components;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.File;
import fr.polytech.entities.models.Workflow;
import fr.polytech.entities.models.WorkflowDetails;
import fr.polytech.entities.models.WorkflowStep;
import fr.polytech.entities.repositories.FileRepository;
import fr.polytech.entities.repositories.WorkflowDetailsRepository;
import fr.polytech.entities.repositories.WorkflowRepository;
import fr.polytech.entities.repositories.WorkflowStepRepository;
import fr.polytech.workflowmanager.errors.WorkflowDetailsNotExist;
import fr.polytech.workflowmanager.errors.WorkflowFieldNotExist;
import fr.polytech.workflowmanager.errors.WorkflowFieldWithNotValueException;
import fr.polytech.workflowmanager.errors.WorkflowFileNotExist;
import fr.polytech.workflowmanager.errors.WorkflowHasNotWorkflowStepException;
import fr.polytech.workflowmanager.errors.WorkflowNotFound;
import fr.polytech.workflowmanager.errors.WorkflowPageOrElementPerPageNotSpecify;
import fr.polytech.workflowmanager.errors.WorkflowStepNotFound;

@Component
@ComponentScan("fr.polytech.entities.repositories")
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class WorkflowBean implements WorkflowManager {

    @Autowired
    WorkflowRepository wr;

    @Autowired
    WorkflowDetailsRepository wdr;

    @Autowired
    WorkflowStepRepository wrs;

    @Autowired
    FileRepository fr;

    @Override
    public List<Workflow> getWorkflows(String field, String value, Integer page, Integer elementPerPage)
            throws WorkflowFieldWithNotValueException, WorkflowFieldNotExist, WorkflowPageOrElementPerPageNotSpecify {
        if ((page != null && elementPerPage == null) || (page == null && elementPerPage != null))
            throw new WorkflowPageOrElementPerPageNotSpecify();
        if (field == null && page == null)
            return (List<Workflow>) wr.findAll();
        else if (field == null)
            return wr.findAll(PageRequest.of(page, elementPerPage));
        try {
            Workflow.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new WorkflowFieldNotExist();
        }
        if (value == null)
            throw new WorkflowFieldWithNotValueException();
        StringBuilder sb = new StringBuilder("%").append(value).append("%");
        Specification<Workflow> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(field),
                sb.toString());
        if (page == null) {
            List<Workflow> workflows = wr.findAll(spec);
            return workflows;
        }
        return wr.findAll(spec, PageRequest.of(page, elementPerPage)).getContent();
    }

    @Override
    public Workflow getWorkflowById(Long id) throws WorkflowNotFound {
        Optional<Workflow> op = wr.findById(id);
        if (op.isPresent())
            return op.get();
        throw new WorkflowNotFound();
    }

    @Override
    public Workflow updateCurrentStep(Long workflowId, Long stepId)
            throws WorkflowNotFound, WorkflowStepNotFound, WorkflowHasNotWorkflowStepException {
        Optional<Workflow> op_workflow = wr.findById(workflowId);
        if (!op_workflow.isPresent())
            throw new WorkflowNotFound();

        Optional<WorkflowStep> op_workflow_step = wrs.findById(stepId);
        if (!op_workflow_step.isPresent())
            throw new WorkflowStepNotFound();

        Workflow wf = op_workflow.get();
        WorkflowStep wfs = op_workflow_step.get();

        if (!wf.getDetails().getSteps().contains(wfs))
            throw new WorkflowHasNotWorkflowStepException();

        wf.setCurrentStep(wfs);
        wr.save(wf);
        return wf;
    }

    @Override
    public Workflow update(Workflow workflow, Long id) throws WorkflowNotFound {
        Optional<Workflow> op = wr.findById(id);
        if (!op.isPresent())
            throw new WorkflowNotFound();

        workflow.setId(id);
        wr.save(workflow);

        return workflow;
    }

    @Override
    public Workflow addAttendees(Administrator user, Long id) throws WorkflowNotFound {
        Optional<Workflow> op = wr.findById(id);
        if (!op.isPresent())
            throw new WorkflowNotFound();

        Workflow w = op.get();
        w.getDetails().getAttendees().add(user);
        wdr.save(w.getDetails());
        return w;
    }

    @Override
    public WorkflowStep getStepById(Long id) {
        Optional<WorkflowStep> op = wrs.findById(id);
        if (op.isPresent())
            return op.get();
        return null;
    }

    @Override
    public WorkflowDetails updateDetails(WorkflowDetails workflowDetails, Long id) throws WorkflowDetailsNotExist {
        Optional<WorkflowDetails> op = wdr.findById(id);
        if (!op.isPresent())
            throw new WorkflowDetailsNotExist();

        workflowDetails.setId(id);
        wdr.save(workflowDetails);

        return workflowDetails;
    }

    @Override
    public Workflow addFile(File file, Long id) throws WorkflowNotFound {
        Optional<Workflow> op = wr.findById(id);
        if (!op.isPresent())
            throw new WorkflowNotFound();
        Workflow workflow = op.get();
        fr.save(file);
        workflow.getDetails().getFiles().add(file);
        wr.save(workflow);

        return workflow;
    }

    @Override
    public Workflow removeFile(Long fileId, Long id) throws WorkflowNotFound, WorkflowFileNotExist {
        Optional<Workflow> op = wr.findById(id);
        if (!op.isPresent())
            throw new WorkflowNotFound();
        Workflow workflow = op.get();

        Optional<File> opt = fr.findById(fileId);
        if (!op.isPresent())
            throw new WorkflowFileNotExist();
        File file = opt.get();
        workflow.getDetails().getFiles().remove(file);
        fr.delete(file);
        wr.save(workflow);

        return workflow;
    }
    
}
