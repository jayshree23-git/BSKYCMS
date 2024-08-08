/**
 * 
 */
package com.project.bsky.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author santanu.barad
 *
 */
public class MailUtils {
	public static Map<String, Object> sendMail(String subject, String body, String recipientMail,
			List<String> ccRecipientMail, String attachedFilePath) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			if (recipientMail == null || recipientMail.isEmpty())
				throw new RuntimeException("Recipient mail address is empty");

			Properties prop = new Properties();
			prop.put("mail.smtp.host", StringUtils.HOST);
			prop.put("mail.smtp.port", StringUtils.PORT);
			prop.put("mail.smtp.auth", StringUtils.AUTH);
			prop.put("mail.smtp.starttls.enable", StringUtils.STARTTLS);

			Session session = Session.getInstance(prop, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(StringUtils.USERNAME, StringUtils.PASSWORD);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(StringUtils.USERNAME));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientMail));
			if (ccRecipientMail != null && !ccRecipientMail.isEmpty()) {
				for (String ccRecipient : ccRecipientMail) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccRecipient));
				}
			}

			message.setSubject(subject);
			message.setText(body);

			if (attachedFilePath != null) {
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(body);

				MimeBodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachedFilePath);
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName(source.getName());

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(attachmentBodyPart);

				message.setContent(multipart);
			}
			Transport.send(message);
			response.put("status", true);
			return response;
		} catch (Exception e) {
			response.put("status", false);
			response.put("errorMessage",
					recipientMail == null ? "Recipient mail address is empty" : String.valueOf(e.getCause()));
			return response;
		}
	}

	public static String getInterViewScheduleMsg(String fromDate, String toDate, String fromTime, String toTime) {
		return "Dear Sir, Your interview has scheduled from date " + fromDate + " to " + toDate + " from time "
				+ fromTime + " to " + toTime + ". Please be available on time. BSKY, Govt. of Odisha";
	}

	public static String getInterViewRejectedMsg() {
		return "Dear Sir\r\n"
				+ "Your application at SHAS as CPD has been Rejected. Kindly login to https://bsky.odisha.gov.in to check your status.";
	}

	public static String getInterviewReScheduledMsg(String fromDate, String toDate, String fromTime, String toTime) {
		return "Dear Sir, Your interview has re-scheduled from date " + fromDate + " to " + toDate + " from time "
				+ fromTime + " to " + toTime + ". Please be available on time. BSKY, Govt. of Odisha";
	}

	public static String getApprovedMsg() {
		return null;
	}

	public static String getFinalApprovedMsg() {
		return null;
	}

	public static String getInterViewScheduleMsgWhatsApp(String fromDate, String toDate, String fromTime,
			String toTime) {
		return "Dear Sir, Your interview has scheduled from date " + fromDate + " to " + toDate + " from time "
				+ fromTime + " to " + toTime + ". Please be available on time. BSKY, Govt. of Odisha";
	}

	public static String getInterViewRejectedMsgWhatsApp() {
		return null;
	}

	public static String getInterviewReScheduledMsgWhatsApp() {
		return null;
	}

	public static String getApprovedMsgWhatsApp() {
		return null;
	}

	public static String getFinalApprovedMsgWhatsApp(String userName, String password) {
		return "Dear Sir" + "Your application at SHAS as CPD has been approved. Your User ID: " + userName
				+ " and Default Password: " + password
				+ ". Please change your password upon login for security purposes.";
	}
}
