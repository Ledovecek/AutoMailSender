package me.ledovec;

import me.zort.sqllib.SQLDatabaseConnection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStream is = Main.class.getResourceAsStream("/message.yaml");
        YamlFile yamlFile = YamlFile.loadConfiguration(is);

        List<String> contacts = List.of("friend@gmail.com", "friend2@gmail.com");

        SQLDatabaseConnection connection = new DatabaseManager().connect();
        new MailSender().sendMail(yamlFile.getString("subject"), yamlFile.getString("content"), connection, contacts);

    }

}