package com.marco.internalresourceservice.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

	//TODO move the "authorisation part" in the security class
	@GetMapping("/simpleapi")
	@PreAuthorize("#oauth2.hasScope('read_internal_service')")
	public String simpleApi() {
		return "<li>This is from the Internal Resource Service, you can access this API only if you have the \"read_internal_service\" scope</li>";
	}
}
