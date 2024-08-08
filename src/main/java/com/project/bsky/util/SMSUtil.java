package com.project.bsky.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SMSUtil {

	public static final ResourceBundle bskyResourceBundle = ResourceBundle.getBundle("application");

	public static String host = decryptCode(bskyResourceBundle.getString("spring.mail.host"));

	public static String port = bskyResourceBundle.getString("spring.mail.port");

	public static String auth = bskyResourceBundle.getString("spring.mail.properties.mail.smtp.auth");

	public static String starttls = bskyResourceBundle.getString("spring.mail.properties.mail.smtp.starttls.enable");

	public static String username = decryptCode(bskyResourceBundle.getString("spring.mail.username"));

	public static String password = decryptCode(bskyResourceBundle.getString("spring.mail.password"));

	public static final String SMS_REQUEST_URL = bskyResourceBundle.getString("sms.url");
	public static final String SMS_ACTION = bskyResourceBundle.getString("sms.action");
	public static final String SMS_DEPARTMENT_ID = bskyResourceBundle.getString("sms.department.id");
	public static final String SMS_TEMPLATE_ID = bskyResourceBundle.getString("sms.template.id");

	public static String sendSms1(String mobileNo, String msg) {
		String sResult = null;
		try {
			// Construct data
//	String phonenumbers="9355429695";
//	String var1 ="Umesh";
//	String var2 ="9355429695";
			//// System.out.println("Sending sms --------");
			String data = "user=" + URLEncoder.encode("csmlcnc", "UTF-8");
			data += "&password=" + URLEncoder.encode("Odishal$38784", "UTF-8");
			data += "&message=" + URLEncoder.encode(msg, "UTF-8");
			data += "&sender=" + URLEncoder.encode("CSMTEC", "UTF-8");
			data += "&mobile=" + URLEncoder.encode(mobileNo, "UTF-8");
			data += "&type=" + URLEncoder.encode("3", "UTF-8");
			data += "&template_id=" + URLEncoder.encode("1207166693912327670", "UTF-8");
			// Send data
			URL url = new URL("http://api.bulksmsgateway.in/sendmessage.php?" + data);
			//// System.out.println("Printing the formed URL as ======= " + data);
			//// System.out.println("Printing the formed URL as ======= " + url);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			String sResult1 = "";
			while ((line = rd.readLine()) != null) {
				// Process line...
				sResult1 = sResult1 + line + " ";
			}
			wr.close();
			rd.close();
			return sResult1;
		} catch (Exception e) {
			//// System.out.println("Error SMS " + e);
			return "Error--------------->>>> " + e;
		}
	}
	// TODO Auto-generated method stub

	public static String decryptCode(String encryptedData) {

		if (encryptedData != null && !encryptedData.isEmpty()) {

			encryptedData = encryptedData.substring(5, encryptedData.length() - 5);

			return new String(Base64.getDecoder().decode(encryptedData));

		} else

			return null;

	}

	public static void sendmail(String subject, String textmessage, String mail) {

		try {
			Properties prop = new Properties();
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			prop.put("mail.smtp.auth", auth);
			prop.put("mail.smtp.starttls.enable", starttls); // TLS

			Session session = Session.getInstance(prop, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
				message.setSubject(subject);
				message.setText(textmessage);
				Transport.send(message);
				// System.out.println("Mail Sent Successfully.");
			} catch (MessagingException e) {
				// System.out.println(e);
			}
		} catch (Exception e) {
			// System.out.println(e);
		}

	}

	public static Map<String, Object> sendOTP(String mobileNo, String message) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("action", SMS_ACTION)
				.addFormDataPart("department_id", SMS_DEPARTMENT_ID).addFormDataPart("template_id", SMS_TEMPLATE_ID)
				.addFormDataPart("sms_content", message).addFormDataPart("phonenumber", mobileNo).build();

		Request request = new Request.Builder().url(SMS_REQUEST_URL).method("POST", body).build();

		Response response = client.newCall(request).execute();

		if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
			String responseBody = response.body().string();
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
		} else
			throw new RuntimeException("Failed to send SMS!");
	}

}