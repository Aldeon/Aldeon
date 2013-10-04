package org.aldeon.utils.config;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Logging {
    public static void configure() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/log4j.properties"));
            PropertyConfigurator.configure(props);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
