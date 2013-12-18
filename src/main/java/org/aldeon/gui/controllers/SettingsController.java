package org.aldeon.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.aldeon.core.CoreModule;
import org.aldeon.core.PropertiesManager;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;

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
    public TextField syncField;
    public TextField peerField;
    public ListView<String> peerList;
    private static final String ADDRESS_TRANSLATION="addressTranslation";
    private static final String IP_AUTO="ipAuto";
    private static final String IP_ADDRESS="ipAddress";
    private static final String PORT_AUTO="portAuto";
    private static final String PORT_NUMBER="portNumber";
    private static final String PRIVACY_LEVEL="privacyLevel";
    private static final String DIFF_TIMEOUT="syncInterval";
    private static final String INIT_PEERS="initPeers";
    private static final int DEFAULT_PORT_NUMBER=41530;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PropertiesManager props=CoreModule.getInstance().getPropertiesManager();
        privacySlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldLvl, Number newLvl) {
                    if(oldLvl.intValue()!=newLvl.intValue()) changedPrivacyLevel(newLvl.intValue());
                }
            });
        peerList.getItems().clear();
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
        if(!peerField.getText().equals("")){
            String ip [] = peerField.getText().split(":");
            if(ip.length==2&&!peerList.getItems().contains(ip[0]+":"+ip[1])){
                if(IpPeerAddress.create(ip[0],Integer.parseInt(ip[1]))!=null)
                    peerList.getItems().add(peerField.getText());
            }
            else {
                if(!peerList.getItems().contains(ip[0]+":"+Integer.toString(DEFAULT_PORT_NUMBER)))
                if(IpPeerAddress.create(ip[0],DEFAULT_PORT_NUMBER)!=null){
                    peerList.getItems().add(peerField.getText()+":"+Integer.toString(DEFAULT_PORT_NUMBER));
                }
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
        for(String peer : peerList.getItems()){
            peers+=peer+",";
        }
        if(peers.length()>0)peers=peers.substring(0,peers.length()-1);
        CoreModule.getInstance().getPropertiesManager().setProperty(INIT_PEERS,peers);
    }

    public void changeDiffTimeout(ActionEvent actionEvent) {
        CoreModule.getInstance().getPropertiesManager().setProperty(DIFF_TIMEOUT,syncField.getText());
    }
}

