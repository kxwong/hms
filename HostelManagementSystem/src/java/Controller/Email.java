package Controller;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {

    public Email(String receipientName, String email, String content) {
        final String username = "wongjc-wa16@student.tarc.edu.my";
        final String password = "5589410abc";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("wongjc-wa16@student.tarc.edu.my"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Daikin Hostel Management");
            message.setText("Dear " + receipientName + content);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
