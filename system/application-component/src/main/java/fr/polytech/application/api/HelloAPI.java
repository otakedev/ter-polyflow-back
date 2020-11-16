package fr.polytech.application.api;

import org.springframework.web.client.RestTemplate;

public class HelloAPI {

	public String getHello() {
		RestTemplate restTemplate = new RestTemplate();
 
        // Send request with GET method and default Headers.
        String hello = restTemplate.getForObject(String.format("http://%s:%s/hello", Api.HOST, Api.PORT), String.class);

        return hello;
	}
    
}
