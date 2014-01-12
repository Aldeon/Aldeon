package org.aldeon.config;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final String PROPERTIES_FILE_NAME = "aldeon.properties";

    private static Configuration currentConfig;

    public static Configuration config() {
        if(currentConfig == null) {
            currentConfig = prepare();
        }
        return currentConfig;
    }

    private static Configuration prepare() {

        Configuration propConf = createPropertiesConfiguration(PROPERTIES_FILE_NAME);
        Configuration defaults = new BaseConfiguration();
        populateWithDefaults(defaults);

        CompositeConfiguration composite = new CompositeConfiguration();
        composite.addConfiguration(propConf, true);
        composite.addConfiguration(defaults);

        return composite;
    }

    private static PropertiesConfiguration createPropertiesConfiguration(String fileName) {

        PropertiesConfiguration propConf;
        try {
            propConf = new PropertiesConfiguration(fileName);
        } catch (ConfigurationException e) {
            log.warn("Failed to retrieve the configuration file. A new configuration will be created.");
            propConf = new PropertiesConfiguration();
            propConf.setFileName(fileName);
            try {
                propConf.save();
            } catch (ConfigurationException e1) {
                log.error("Failed to create the configuration file", e1);
            }
        }

        propConf.setAutoSave(true);
        return propConf;
    }

    private static void populateWithDefaults(Configuration configuration) {
        configuration.setProperty("port.value",    "41530");
        configuration.setProperty("port.randomize", "false");

        configuration.setProperty("upnp.enabled",   "true");
        configuration.setProperty("upnp.always",    "false");

        configuration.setProperty("peers.local-discovery.enabled", "true");

        configuration.addProperty("peers.initial", "aldeon.org:80");

        configuration.setProperty("sync.intervals.diff", "15000");

    }
}
