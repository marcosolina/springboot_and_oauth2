package com.marco.authorisationservice.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarcoController {

    /**
     * API used by the other services to retrieve the user details
     * 
     * @param principal
     * @return
     */
    @GetMapping(value = "/userinfo")
    public Map<String, Object> user(Principal principal) {
        HashMap<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("username", principal.getName());
        return userInfoMap;

    }
}
