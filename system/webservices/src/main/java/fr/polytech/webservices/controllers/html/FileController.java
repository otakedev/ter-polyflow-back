package fr.polytech.webservices.controllers.html;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@SpringBootApplication
public class FileController {
    
    @ApiIgnore
    @CrossOrigin
    @GetMapping("/file.html")
    public String index() {
        return "file";
    }
}