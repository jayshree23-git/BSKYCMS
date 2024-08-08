package com.project.bsky.util;

import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
	public static final ResourceBundle bskyResourceBundle = ResourceBundle.getBundle("application");
	public static final String HOST = bskyResourceBundle.getString("spring.mail.host");
	public static final String PORT = bskyResourceBundle.getString("spring.mail.port");
	public static final String AUTH = bskyResourceBundle.getString("spring.mail.properties.mail.smtp.auth");
	public static final String STARTTLS = bskyResourceBundle
			.getString("spring.mail.properties.mail.smtp.starttls.enable");

	public static final String USERNAME = bskyResourceBundle.getString("spring.mail.username");
	public static final String PASSWORD = bskyResourceBundle.getString("spring.mail.password");

	@Autowired
	private JavaMailSender sender;

	// public void sendmail(String toaddress, String fpath)
	public Integer sendmail(String mailId, String msg) {
		MimeMessage configmess = sender.createMimeMessage();
		Integer status;
		try {
			MimeMessageHelper mm = new MimeMessageHelper(configmess, false);
			mm.setTo(mailId);
			mm.setSubject("Hi Umesh This Is Testing ");
			mm.setText(msg);
			// mm.addAttachment("Filename" , "Filepath");
			sender.send(configmess);
			status = 1;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = 0;
		}
		return status;
	}

	public static void sendOtpLoginMail(String subject, String body, String recipientMail, List<String> ccRecipientMail,
			String attachedFilePath) throws MessagingException {
		try {
			if (recipientMail != null && !recipientMail.isEmpty()) {
				Properties prop = new Properties();
				prop.put("mail.smtp.host", HOST);
				prop.put("mail.smtp.port", PORT);
				prop.put("mail.smtp.auth", AUTH);
				prop.put("mail.smtp.starttls.enable", STARTTLS);

				Session session = Session.getInstance(prop, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(USERNAME, PASSWORD);
					}
				});

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(USERNAME));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientMail));

				if (ccRecipientMail != null && !ccRecipientMail.isEmpty()) {
					for (String ccRecipient : ccRecipientMail) {
						message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccRecipient));
					}
				}

				message.setSubject(subject);
				Transport.send(message);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
