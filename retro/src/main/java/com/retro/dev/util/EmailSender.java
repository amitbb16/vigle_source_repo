package com.retro.dev.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailSender {


	@Value("${app.default-email-sender}")
	private String defaultMailSender;

	
	
    public final void prepareAndSendEmailUsingUNandPwd(String htmlMessage, String toMailId) {

        //final OneMethod oneMethod = new OneMethod();
        final List<String> resourceList = new ArrayList<>();
        resourceList.add(defaultMailSender);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);	//standard port for SMTP with TLS

        mailSender.setUsername(String.valueOf(resourceList.get(0)));
        mailSender.setPassword(String.valueOf(resourceList.get(1)));

        //from email id and password
        System.out.println("Username is : " + String.valueOf(resourceList.get(0)).split("@")[0]);
        System.out.println("Password is : " + String.valueOf(resourceList.get(1)));

        Properties mailProp = mailSender.getJavaMailProperties();
        mailProp.put("mail.transport.protocol", "smtp");
        mailProp.put("mail.smtp.auth", "true");
        mailProp.put("mail.smtp.starttls.enable", "true");
        mailProp.put("mail.smtp.starttls.required", "true");
        mailProp.put("mail.debug", "true");
        mailProp.put("mail.smtp.ssl.enable", "true");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(toMailId);
            helper.setSubject("Welcome to Subject Part");
            helper.setText(htmlMessage, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
