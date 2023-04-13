package me.ledovec;

import me.zort.sqllib.SQLDatabaseConnection;
import me.zort.sqllib.api.data.Row;
import org.simpleyaml.configuration.file.YamlFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MailSender {

    private final YamlFile YAML_FILE;
    {
        try {
            YAML_FILE = YamlFile.loadConfiguration(getClass().getResourceAsStream("/credentials.yaml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final String USERNAME = YAML_FILE.getString("email.username");
    private final String PASSWORD = YAML_FILE.getString("email.password");
    private final String SMTP_HOST = YAML_FILE.getString("email.host");
    private final int SMTP_PORT = YAML_FILE.getInt("email.port");

    public void sendMail(String subject, String content, SQLDatabaseConnection connection, List<String> recipients) {
        recipients.forEach(recipient -> {
            Optional<Row> result = connection.select("id").from("saved_contacts").where().isEqual("contact", recipient).obtainOne();
            if (result.isEmpty()) {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", SMTP_HOST);
                props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(USERNAME));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                    message.setHeader("Content-Type", "text/plain; charset=UTF-8");
                    message.setSubject(subject);
                    message.setContent(content, "text/html; charset=UTF-8");

                    Transport.send(message);
                    connection.insert().into("saved_contacts", "contact").values(recipient).execute();

                    System.out.println("Sent - " + recipient);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Skipping " + recipient + " - already contacted.");
            }
        });
    }

}
