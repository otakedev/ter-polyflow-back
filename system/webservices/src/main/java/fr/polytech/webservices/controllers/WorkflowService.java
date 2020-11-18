package fr.polytech.webservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.workflow.models.Workflow;
import fr.polytech.workflowmanager.components.WorkflowManager;

import java.util.List;

@RestController
@SpringBootApplication
@RequestMapping("/api/admin")
@ComponentScan({ "fr.polytech.workflowmanager" })
public class WorkflowService {

    @Autowired
    WorkflowManager wm;
    
    @CrossOrigin
    @GetMapping("/workflow")
    public List<Workflow> getWorkflows() {
        return wm.getWorkflows();
    }
}
