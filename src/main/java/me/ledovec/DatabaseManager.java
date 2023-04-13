package me.ledovec;

import me.zort.sqllib.SQLConnectionBuilder;
import me.zort.sqllib.SQLDatabaseConnection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class DatabaseManager {

    private final YamlFile YAML_FILE;

    {
        try {
            YAML_FILE = YamlFile.loadConfiguration(getClass().getResourceAsStream("/credentials.yaml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final String ADDRESS = YAML_FILE.getString("mysql.address");
    private final int PORT = YAML_FILE.getInt("mysql.port");
    private final String DATABASE = YAML_FILE.getString("mysql.database");
    private final String USERNAME = YAML_FILE.getString("mysql.username");
    private final String PASSWORD = YAML_FILE.getString("mysql.password");

    public SQLDatabaseConnection connect() {
        SQLDatabaseConnection connection = new SQLConnectionBuilder(ADDRESS, PORT, DATABASE, USERNAME, PASSWORD).build();
        connection.exec(() -> "CREATE TABLE IF NOT EXISTS `saved_contacts` (" +
                "`id` BIGINT(255) NOT NULL AUTO_INCREMENT," +
                "`contact` TEXT NULL," +
                "PRIMARY KEY (`id`)" +
                ")" +
                "COLLATE='utf8mb4_general_ci'" +
                ";");
        if (connection.connect()) return connection;
        return null;
    }

}
