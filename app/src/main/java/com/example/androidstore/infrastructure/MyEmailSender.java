package com.example.androidstore.infrastructure;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MyEmailSender
{
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@gmail.com", Pattern.CASE_INSENSITIVE);
	
	public void SendMail(String body, String address, String username, String password) throws MessagingException, IllegalArgumentException
	{
		final PasswordAuthentication pAuth = new PasswordAuthentication(username, password);
		
		if (!validateAddress(address))
		{
			throw new IllegalArgumentException("Address in incorrect format");
		}
		
		Address internetAddress = new InternetAddress(address);
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(props, 
					new Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication()
						{
							return pAuth;
						}
					});
		
		Message message = new MimeMessage(session);
		message.setFrom(internetAddress);
		message.setRecipient(Message.RecipientType.TO, internetAddress);
		message.setSubject("New order");
		message.setText(body);
		message.setHeader("Content-Type", "text/plain; charset=UTF-8");
		
		Transport.send(message);
	}
	
	private static boolean validateAddress(String address)
	{
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(address);
		return matcher.find();
	}
}
