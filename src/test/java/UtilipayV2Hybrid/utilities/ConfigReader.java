package UtilipayV2Hybrid.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try {
            // Read env system property; default to "dev"
            String env = System.getProperty("env", "dev").toLowerCase();
            String configFile = "src/test/resources/config-" + env + ".properties";
            FileInputStream input = new FileInputStream(configFile);
            properties.load(input);
            input.close();
            System.out.println("Loaded config file: " + configFile);
            System.out.println();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file for environment", e);
        }
    }
    

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // fallback only if property missing
        String envValue = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        return null;
    }

}
