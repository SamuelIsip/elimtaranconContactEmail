package com.elimtarancon.email.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.elimtarancon.email.dto.ElimUser;
import com.elimtarancon.email.services.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	/**
	 * Endpoint to send the email with data validation
	 * @param elimUser
	 * @return
	 */
	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@Valid @RequestBody ElimUser elimUser) {
		
		try {
			emailService.checkCaptcha(elimUser.getGRecaptchaResponse());
			emailService.sendEmail(elimUser);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
