package com.essco.seller.Clases;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/* Esta clase permite el envio de correos electronicos en un segundo plano y
 directamente desde la app mediante la ayuda de la configuaracion smtp de un correo*/
public class Class_MailJob extends AsyncTask<Class_MailJob.Mail, Void, Void> {
    private final String user;
    private final String pass;

    public Class_MailJob(String user, String pass) {
        super();
        this.user = user;
        this.pass = pass;
    }

    @Override
    protected Void doInBackground(Mail... mails) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });


        for (Mail mail : mails) {

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mail.from));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(mail.to));
                message.setSubject(mail.subject);
                message.setText(mail.content);

                Transport.send(message);

            } catch (MessagingException e) {
                Log.d("Class_MailJob", e.getMessage());
            }
        }
        return null;
    }

    public static class Mail {
        private final String subject;
        private final String content;
        private final String from;
        private final String to;

        public Mail(String from, String to, String subject, String content) {
            this.subject = subject;
            this.content = content;
            this.from = from;
            this.to = to;
        }
    }
}
