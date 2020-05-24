package com.marco.resourceservice.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

	//TODO move the "authorisation part" in the security class
    @GetMapping("/read")
    @PreAuthorize("#oauth2.hasScope('read')")
    public String readResource() {
        return "You can access this API only if you have the \"read\" scope";
    }
}
