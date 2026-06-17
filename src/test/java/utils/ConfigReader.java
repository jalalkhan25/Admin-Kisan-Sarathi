package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader.java
 * =================
 * This utility class reads values from config.properties file.
 * Instead of hardcoding URLs, usernames, passwords in test code,
 * we store them in config.properties and read from here.
 *
 * Usage: String url = ConfigReader.getProperty("url");
 */
public class ConfigReader {

    // Properties object to hold all key-value pairs from file
    private static Properties properties;

    // Static block - runs once when class is first used
    static {
        try {
            // Path to the config.properties file
            String filePath = "src/test/resources/config.properties";

            // Read the file
            FileInputStream fileInput = new FileInputStream(filePath);
            properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

        } catch (IOException e) {
            System.out.println("ERROR: Could not read config.properties file!");
            System.out.println("Make sure the file exists at: src/test/resources/config.properties");
            e.printStackTrace();
        }
    }

    /**
     * Get a value from config.properties by key name.
     *
     * @param key - the property name (e.g., "url", "username", "password")
     * @return the value from config.properties
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
