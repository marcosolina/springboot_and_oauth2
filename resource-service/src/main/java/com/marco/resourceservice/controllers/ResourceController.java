package com.marco.resourceservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ResourceController {
	
	@Autowired
	private WebClient webClient;

	//TODO move the "authorisation part" in the security class
    @GetMapping("/read")
    @PreAuthorize("#oauth2.hasScope('read')")
    public String readResource() {
    	String s = webClient.get().uri("http://localhost:8082/internalservice/simpleapi").retrieve().bodyToMono(String.class).block();
    	
        return "<li>This is from the Resource service, you can access this API only if you have the \"read\" scope</li>" + s;
    }
}
