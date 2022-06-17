package com.elimtarancon.email.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonPropertyOrder({ "name", "tlf", "email", "message", "g-recaptcha-response"})
public class ElimUser {

	@NotNull
	@JsonProperty("name")
	private String name;
	@NotNull
	@JsonProperty("tlf")
	private String tlf;
	@Email
	@NotNull
	@JsonProperty("email")
	private String email;
	@Size(min = 5, max = 260)
	@NotNull
	@JsonProperty("message")
	private String message;
	@NotNull
	@JsonProperty("g-recaptcha-response")
	private String gRecaptchaResponse;
	
}
