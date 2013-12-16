package org.aldeon.core;

import javafx.beans.property.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Prophet on 15.12.13.
 */
public class PropertiesManager {
    private static final String CONFIG_NAME="aldeon.conf";
    private static final Properties props = new Properties();

    public PropertiesManager(){
        File config = new File(CONFIG_NAME);
        if( config.exists())
            try {
                props.load(new FileInputStream(config));
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            try {
                props.store(new FileOutputStream(CONFIG_NAME),null);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void setProperty(String property, Property value){
        props.put(property, value.getValue().toString());
        saveChange();
    }

    public String getProperty(String property){
        return props.getProperty(property);
    }

    public void saveChange(){
        try {
            props.store(new FileOutputStream(CONFIG_NAME),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
