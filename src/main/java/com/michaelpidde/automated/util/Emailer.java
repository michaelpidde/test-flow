package com.michaelpidde.automated.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer {
	
	public static void send(String address, String messageBody) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		// TODO Put the mail server in the config XML.
		props.put("mail.smtp.host", "---");
		Session session = Session.getDefaultInstance(props, null);
		
		try {
			Message message = new MimeMessage(session);
			// TODO Put the from address below in the config XML. 
			message.setFrom(new InternetAddress("michael.pidde@gmail.com", "Regression Testing Notifier"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(address, ""));
			message.setSubject("Regression Testing Results");
			message.setText(messageBody);
			Transport.send(message);
		} catch(AddressException e) {
			System.out.println(e.toString());
		} catch(MessagingException e) {
			System.out.println(e.toString());
		} catch(UnsupportedEncodingException e) {
			System.out.println(e.toString());
		}
	}
}