package com.marco.webappclientservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarcoController {

    @GetMapping("/hello")
    public String getHello() {
        return "Hello from Web App Client (no admin)";
    }

    @GetMapping("/admin/hello")
    public String getHelloAdmin() {
        return "Hello Admin from Web App Client (admin)";
    }

}
