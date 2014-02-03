/*
 * TestFlow email handler
 *
 * Copyright (C) 2014 Michael Pidde <michael.pidde@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.util;

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