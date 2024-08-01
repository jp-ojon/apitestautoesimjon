package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Class to read config.properties which stores sensitive information
public class ConfigFileReader {
	private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigFileReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                // Log a message or handle the absence of the file
                System.out.println("Sorry, unable to find config.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

	/**
	 * Gets the API Key from the config.properties files
	 * @return apiKey
	 */
    public static String getApiKey() {
        return properties.getProperty("apiKey", "defaultApiKey"); // Fallback value
    }

	/**
	 * Gets the API Secret from the config.properties files
	 * @return apiSecret
	 */
    public static String getApiSecret() {
        return properties.getProperty("apiSecret", "defaultApiSecret"); // Fallback value
    }
}
