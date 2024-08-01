package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Class to read config.properties which stores sensitive information
public class ConfigFileReader {
	private static final Properties properties = new Properties();
	private static boolean loaded = false;

	static {
		try (InputStream input = ConfigFileReader.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				// Load properties file from config.properties
				properties.load(input);
				loaded = true;
			} else {
				System.err.println("config.properties not found in path src/test/resources");
			}
		} catch (IOException ex) {
			System.err.println("Error loading config.properties");
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the API Key from the config.properties files
	 * @return apiKey
	 */
	public static String getApiKey() {
		if (!loaded) {
			throw new IllegalStateException("Configuration file not loaded");
		}
		return properties.getProperty("apiKey");
	}

	/**
	 * Gets the API Secret from the config.properties files
	 * @return apiSecret
	 */
	public static String getApiSecret() {
		if (!loaded) {
			throw new IllegalStateException("Configuration file not loaded");
		}
		return properties.getProperty("apiSecret");
	}
}
