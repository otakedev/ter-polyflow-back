package fr.polytech.workflowmanager.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflow.repositories.WorkflowRepository;

@Component
@ComponentScan("fr.polytech.workflow.repositories")
@EntityScan("fr.polytech.workflow.models")
@EnableJpaRepositories("fr.polytech.workflow.repositories")
public class WorkflowBean implements WorkflowManager {

    @Autowired
    WorkflowRepository wr;

    @Override
    public List<Workflow> getWorkflows() {
        return (List<Workflow>) wr.findAll();
    }
    
}
