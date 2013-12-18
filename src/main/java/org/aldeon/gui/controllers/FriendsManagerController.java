package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import org.aldeon.core.CoreModule;
import org.aldeon.crypt.Key;
import org.aldeon.events.Callback;
import org.aldeon.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FriendsManagerController implements Initializable{
    public FlowPane fpane;
    private Map<Key,User> users= new HashMap<>();
    private Map<Key,Parent> guiIds= new HashMap<>();


    public void showIdentities(){
        CoreModule.getInstance().getUserManager().refreshUsers();
        users=CoreModule.getInstance().getUserManager().getAllUsers();
        for(User usr : users.values()){
            Parent childNode = constructUser(usr);
            fpane.getChildren().add(childNode);
            guiIds.put(usr.getPublicKey(), childNode);
        }
    }

    private Parent constructUser(User usr) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/Friend.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/Friend.fxml").openStream());
        } catch (IOException e){}
        FriendController fc = loader.getController();
        final FriendController fcF = fc;
        fc.setUser(usr,this);
        guiIds.put(usr.getPublicKey(),parent);
        return parent;
    }

    public void renameUser(final User usr){
        CoreModule.getInstance().getUserManager().renameUser(usr,new Callback<Boolean>() {
            @Override
            public void call(Boolean val) {
                fpane.getChildren().remove(guiIds.get(usr.getPublicKey()));
                fpane.getChildren().add(constructUser(usr));
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        users=CoreModule.getInstance().getUserManager().getAllUsers();
        showIdentities();

    }
}
