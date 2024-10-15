package com.example.desktopapprestaurant;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailSenderClass {

    private String reciver; // Todo Correo del destinatario
    private String authUser; // Todo correo del remitente
    private String password = "oiyv yklb mfsz tfoa"; // Tu contraseña real

    private Properties properties;

    public MailSenderClass() {
        // Propiedades del servidor SMTP
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public void enviarIncidencia(String titulo, String cuerpo) {

        String subject = titulo;
        String receiver = reciver;
        String content = cuerpo;

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authUser, password);
            }
        });

        try {
            Message message = createMessage(session,receiver,subject,content);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error enviando el correo.");
        }
    }




    private Message createMessage(Session session,String receiver,String subject, String content) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
}

