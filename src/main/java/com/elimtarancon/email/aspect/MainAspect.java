package com.elimtarancon.email.aspect;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MainAspect {

	private Logger logger = Logger.getLogger(MainAspect.class.getName());

	@Pointcut("execution(* com.elimtarancon.email.controller.EmailController.sendEmail(..))")
	private void forControllerPackage() {
		// Only for logging propose
	}

	@Pointcut("execution(* com.elimtarancon.email.services.EmailServiceImpl.checkCaptcha(..))")
	private void forServiceCheckCaptcha() {
		// Only for logging propose
	}

	@Pointcut("execution(* com.elimtarancon.email.services.EmailServiceImpl.responseSanityCheck(..))")
	private void forServiceMatchReCaptcha() {
		// Only for logging propose
	}

	@Around("forControllerPackage()")
	public Object timestampSendEmail(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		String method = proceedingJoinPoint.getSignature().toShortString();
		logger.info("Executing @AroundMethod advice on method: " + method);

		long timestamp = System.currentTimeMillis();

		Object result = null;

		try {
			result = proceedingJoinPoint.proceed();
		} catch (Exception ex) {
			logger.log(Level.INFO, "No timestamp! The email was not sent!");
			throw ex;
		}

		logger.log(Level.INFO, "Email was sent in: {} miliseconds", (System.currentTimeMillis() - timestamp));

		return result;
	}

	@AfterThrowing(pointcut = "forControllerPackage()", throwing = "excep")
	private void exceptionEmailSend(JoinPoint jointPoint, Throwable excep) {
		String method = jointPoint.getSignature().toShortString();
		logger.info("Executing @AfterThrowing advice on method: " + method);
		logger.log(Level.SEVERE, excep, () -> "Email was not sent: " + excep.getMessage());
	}

	@AfterThrowing(pointcut = "forServiceCheckCaptcha()", throwing = "excep")
	private void exceptionCheckCaptcha(JoinPoint jointPoint, Throwable excep) {
		String method = jointPoint.getSignature().toShortString();
		logger.info("Executing @AfterThrowing advice on method: " + method);
		logger.log(Level.SEVERE, excep, () -> "Captcha was not verify correctly: " + excep.getMessage());
	}

	@AfterReturning(pointcut = "forServiceMatchReCaptcha()", returning = "result")
	public void matchReCaptcha(JoinPoint joinPoint, Object result) {
		String method = joinPoint.getSignature().toShortString();
		logger.info("Executing @AfterReturning advice on method: " + method);
		logger.log(Level.INFO, "ReCaptcha response form user match with pattern: {}", result);
	}

}
