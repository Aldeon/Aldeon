package org.aldeon.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.aldeon.core.CoreModule;
import org.aldeon.core.PropertiesManager;
import org.aldeon.protocol.Action;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
    public TextField syncField;
    public TextField peerField;
    public ListView peerList;
    private static final String ADDRESS_TRANSLATION="addressTranslation";
    private static final String IP_AUTO="ipAuto";
    private static final String IP_ADDRESS="ipAddress";
    private static final String PORT_AUTO="portAuto";
    private static final String PORT_NUMBER="portNumber";
    private static final String PRIVACY_LEVEL="privacyLevel";
    private static final String DIFF_TIMEOUT="syncInterval";
    private static final String INIT_PEERS="initPeers";

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
        if(!props.getIpAutodetection()){
            selectedIp.setSelected(true);
            ipField.setDisable(false);
            ipField.setText(props.getMyIp().toString());
        }
        if(!props.getPortRandomization()){
            selectedPort.setSelected(true);
            portField.setDisable(false);
            portField.setText(Integer.toString(props.getPortNumber()));
        }
        if(props.getAddressTranslation())
            addressTranslation.setSelected(true);
        privacySlider.setValue(props.getPrivacyLevel());
        syncField.setText(Integer.toString(props.getDiffTimeout()));
        peerList.getItems().addAll(props.getPeersForGUI());
    }

    public void pathChange(ActionEvent actionEvent) {
    }

    public void ipRadioClicked(ActionEvent actionEvent) {
        if(((RadioButton)actionEvent.getTarget()).getId().equals("autodetectIp")){
            ipField.setText("");
            ipField.setDisable(true);
            CoreModule.getInstance().getPropertiesManager().removeProperty(IP_ADDRESS);
        }else{
            ipField.setDisable(false);
        }
        CoreModule.getInstance().getPropertiesManager().setProperty(IP_AUTO,Boolean.toString(ipField.isDisabled()));
    }

    public void portRadioClicked(ActionEvent actionEvent) {
        if(((RadioButton)actionEvent.getTarget()).getId().equals("randomPort")){
            portField.setDisable(true);
            portField.setText("");
            CoreModule.getInstance().getPropertiesManager().removeProperty(PORT_NUMBER);
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

    public void addPeer(ActionEvent actionEvent) {
        if(!peerField.getText().equals("")&&!peerList.getItems().contains(peerField.getText())){
            try {
                if(InetAddress.getByName(peerField.getText())!=null)peerList.getItems().add(peerField.getText());
            } catch (UnknownHostException e) {
                System.out.println("IP ADDRESS INCORRECT");
            }
        }
        peerField.setText("");
        refreshPeers();
    }

    public void removePeer(ActionEvent actionEvent) {
        peerList.getItems().remove(peerList.getSelectionModel().getSelectedItem());
        peerList.getSelectionModel().clearSelection();
        refreshPeers();
    }

    public void refreshPeers(){
        String peers="";
        for(String peer : (List<String>)peerList.getItems()){
            peers+=peer+",";
        }
        peers=peers.substring(0,peers.length()-1);
        CoreModule.getInstance().getPropertiesManager().setProperty(INIT_PEERS,peers);
    }

    public void changeDiffTimeout(ActionEvent actionEvent) {
        CoreModule.getInstance().getPropertiesManager().setProperty(DIFF_TIMEOUT,syncField.getText());
    }
}

