package com.data.classifier.utility;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil
{
    private static final String SENDER = "SenderEmail";
    private static final String SUBJECT = "YOUR OTP";
    private static final String SMTP_HOST = "mail.smtp.host";
    private static final String GMAIL_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_PASSWORD = "SenderPassword";
    

    public static boolean sendmail(String toAddress, String bodyContent)
    {
        boolean result = true;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put(SMTP_HOST, GMAIL_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
       
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                  protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(SENDER, SENDER_PASSWORD);
                  }
                });

        try
        {
            MimeMessage message = new MimeMessage(session); 
            message.setFrom(new InternetAddress(SENDER));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress)); 
            message.setSubject(SUBJECT);
            message.setText(bodyContent);

            Transport.send(message);
            System.out.println("Mail successfully sent");
        }
        catch (Exception ex)
        {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    public static int getOTP()
    {
        Random r = new Random(System.currentTimeMillis());
        return 10000 + r.nextInt(20000);
    }
}
