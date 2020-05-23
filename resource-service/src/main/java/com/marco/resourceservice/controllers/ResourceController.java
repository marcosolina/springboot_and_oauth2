package com.marco.resourceservice.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/read")
    @PreAuthorize("#oauth2.hasScope('read')")
    public String readResource() {
        return "This is a resource read only";
    }
}
