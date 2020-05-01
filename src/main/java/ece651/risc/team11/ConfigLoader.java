package ece651.risc.team11;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * This class is for loading properties from files.
 */
public class ConfigLoader {
    private Properties configFile;
    private static ConfigLoader instance;

    private ConfigLoader() {
        configFile = new java.util.Properties();
        try {
            configFile.load(
                    Objects.requireNonNull(this.getClass().getClassLoader()
                            .getResourceAsStream("config.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getValue(String key) {
        return configFile.getProperty(key);
    }

    public static String getProperty(String key) {
        if (instance == null) instance = new ConfigLoader();
        return instance.getValue(key);
    }
}