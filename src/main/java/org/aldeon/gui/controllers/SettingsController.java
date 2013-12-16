package org.aldeon.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.aldeon.core.CoreModule;
import org.aldeon.core.PropertiesManager;
import org.aldeon.protocol.Action;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public GridPane content;
    public BorderPane settings;
    public CheckBox addressTranslation;
    public TextField portField;
    public TextField ipField;
    public Slider privacySlider;
    public TextField pathField;
    public RadioButton autodetectIp;
    public RadioButton selectedIp;
    public RadioButton selectedPort;
    public RadioButton randomPort;
    private int optCount;
    private static final String ADDRESS_TRANSLATION="addressTranslation";
    private static final String IP_AUTO="ipAuto";
    private static final String IP_ADDRESS="ipAddress";
    private static final String PORT_AUTO="portAuto";
    private static final String PORT_NUMBER="portNumber";
    private static final String PRIVACY_LEVEL="privacyLevel";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PropertiesManager props=CoreModule.getInstance().getPropertiesManager();
        privacySlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldLvl, Number newLvl) {
                    if(oldLvl.intValue()!=newLvl.intValue()) changedPrivacyLevel(newLvl.intValue());
                }
            });
        refreshSettings();
    }

    public void refreshSettings(){
        PropertiesManager props = CoreModule.getInstance().getPropertiesManager();
        if(!Boolean.parseBoolean(props.getProperty(IP_AUTO))){
            selectedIp.setSelected(true);
            ipField.setDisable(false);
            String ip = props.getProperty(IP_ADDRESS);
            ipField.setText(ip);
        }
        if(!Boolean.parseBoolean(props.getProperty(PORT_AUTO))){
            selectedPort.setSelected(true);
            portField.setDisable(false);
            String ip = props.getProperty(PORT_NUMBER);
            portField.setText(ip);
        }
        if(Boolean.parseBoolean(props.getProperty(ADDRESS_TRANSLATION)))
            addressTranslation.setSelected(true);
        String privacy = props.getProperty(PRIVACY_LEVEL);
        if(privacy!=null){
            privacySlider.setValue(Integer.parseInt(props.getProperty(PRIVACY_LEVEL)));
        }
    }

    public void pathChange(ActionEvent actionEvent) {
    }

    public void ipRadioClicked(ActionEvent actionEvent) {
        if(((RadioButton)actionEvent.getTarget()).getId().equals("autodetectIp")){
            ipField.setText("");
            ipField.setDisable(true);
            CoreModule.getInstance().getPropertiesManager().setProperty(IP_ADDRESS, null);
        }else{
            ipField.setDisable(false);
        }
        CoreModule.getInstance().getPropertiesManager().setProperty(IP_AUTO,Boolean.toString(ipField.isDisabled()));
    }

    public void portRadioClicked(ActionEvent actionEvent) {
        if(((RadioButton)actionEvent.getTarget()).getId().equals("randomPort")){
            portField.setDisable(true);
            portField.setText("");
            CoreModule.getInstance().getPropertiesManager().setProperty(PORT_NUMBER,null);
        }
        else
            portField.setDisable(false);
        CoreModule.getInstance().getPropertiesManager().setProperty(PORT_AUTO,Boolean.toString(portField.isDisabled()));
    }

    public void addressTranslationClicked(ActionEvent actionEvent) {
        CoreModule.getInstance().getPropertiesManager().setProperty(ADDRESS_TRANSLATION,addressTranslation.selectedProperty().getValue().toString());
    }

    public void changePort(ActionEvent actionEvent) {
        if(!portField.isDisable()){
            CoreModule.getInstance().getPropertiesManager().setProperty(PORT_NUMBER,portField.getText());
        }
    }

    public void changeIp(ActionEvent actionEvent) {
        if(!ipField.isDisable()){
            CoreModule.getInstance().getPropertiesManager().setProperty(IP_ADDRESS,ipField.getText());
        }
    }

    public void changedPrivacyLevel(int newLevel){
        CoreModule.getInstance().getPropertiesManager().setProperty(PRIVACY_LEVEL,Integer.toString(newLevel));
    }
}

