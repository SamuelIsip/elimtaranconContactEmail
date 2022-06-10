package com.elimtarancon.email.services;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

import com.elimtarancon.email.dto.ElimUser;

public interface EmailService {
	
	/**
	 * Check if captcha code is valid
	 * @param gRecaptcha
	 * @throws Exception 
	 */
	public void checkCaptcha(@NotNull String gRecaptcha);
	
	/**
	 * Add data to message and send it
	 * @param elimUser
	 * @throws MessagingException
	 */
	public void sendEmail(ElimUser elimUser);

}
