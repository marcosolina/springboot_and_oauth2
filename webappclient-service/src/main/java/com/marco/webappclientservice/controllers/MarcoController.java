package com.marco.webappclientservice.controllers;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class MarcoController {

    @Autowired
    private WebClient webClient;
    
    @GetMapping("/hello")
    public String getHello() {
        String s = webClient.get().uri("http://localhost:8084/read").retrieve().bodyToMono(String.class).block();
        return "Hi, you can reach this page if you have either the \"ADMIN\" or \"USER\" role " + s;
    }

    @GetMapping("/admin/hello")
    public String getHelloAdmin() {
        String s = webClient.get().uri("http://localhost:8084/read").retrieve().bodyToMono(String.class).block();
        return "Hi, you can reach this page only if you have the \"ADMIN\" role " + s;
    }
    
    @GetMapping(value="/userinfo")
    public HashMap<String, Object> user( Principal principal) {
       
      System.err.println("UserInfoController.user()"); 
      
            HashMap<String, Object>  userInfoMap= new HashMap<>();
            userInfoMap.put("username", principal.getName());
            userInfoMap.put("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            
          return userInfoMap;
     
    }
}
