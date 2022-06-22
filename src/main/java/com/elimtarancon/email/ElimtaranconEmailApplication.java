package com.elimtarancon.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ElimtaranconEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElimtaranconEmailApplication.class, args);
	}

	@Value("${elimTarancon.email}")
	private String elimEmail;
	@Value("${elimTarancon.pass}")
	private String elimPass;
	@Value("${mail.smtp.auth}")
	private String mailSmtpAuth;
	@Value("${mail.smtp.starttls.enable}")
	private String mailSmtpStarttlsEnable;
	@Value("${mail.smtp.host}")
	private String mailSmtpHost;
	@Value("${mail.smtp.port}")
	private String mailSmtpPort;

	@Bean
	public Session getMailConfiguration() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", mailSmtpAuth);
		props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
		props.put("mail.smtp.host", mailSmtpHost);
		props.put("mail.smtp.port", mailSmtpPort);

		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(elimEmail, elimPass);
			}
		});

	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
