package com.elimtarancon.email.services;

import java.net.URI;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.elimtarancon.email.dto.ElimUser;
import com.elimtarancon.email.dto.GoogleResponse;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private Session session;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${recaptcha.secret}")
	private String reCaptchaSecret;

	private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

	@Value("${elimTarancon.email}")
	private String elimEmail;

	@Override
	public void sendEmail(ElimUser elimUser) {
		try {

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(elimEmail, false));

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(elimEmail));
			msg.setSubject("Contact ElimTarancon");
			msg.setContent(getHTMLMessage(elimUser), "text/html; charset=utf-8");
			msg.setSentDate(new Date());

			Transport.send(msg);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The email could not be sent!");
		}

	}

	/**
	 * @param elimUser
	 * @return Message formated with HTML-code emojis and spaces
	 */
	private String getHTMLMessage(ElimUser elimUser) {
		return String.format(
				"<h3>&#129492;&nbsp;Nume:&nbsp;%s</h3><br><h3>&#128241;&nbsp;Telefon:&nbsp;%s</h3><br><h3>&#9993;&nbsp;Email:&nbsp;%s</h3><br><h3>"
						+ "&#9999;&nbsp;Mesaj:&nbsp;%s</h3>",
				elimUser.getName(), elimUser.getTlf(), elimUser.getEmail(), elimUser.getMessage());
	}

	@Override
	public void checkCaptcha(String gRecaptcha) {
		if (!responseSanityCheck(gRecaptcha))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The response captcha code is not valid!");

		URI verifyUri = URI.create(String.format(
				"https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s", reCaptchaSecret, gRecaptcha));

		Optional<GoogleResponse> googleResponse = Optional
				.ofNullable(restTemplate.getForObject(verifyUri, GoogleResponse.class));

		if (!googleResponse.isPresent())
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "There is not data response from google!");

		if (!googleResponse.get().isSuccess())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "There validation of Captcha was unsuccessful!");

	}

	/**
	 * Check if the reCaptcha response form user match with pattern
	 * 
	 * @param response
	 * @return
	 */
	private boolean responseSanityCheck(String response) {
		return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
	}

}
