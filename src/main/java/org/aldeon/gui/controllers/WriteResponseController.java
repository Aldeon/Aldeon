package org.aldeon.gui.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.aldeon.core.CoreModule;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.gui.colors.ColorManager;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;

import java.util.*;

/**
 *
 */
public class WriteResponseController {
    public Button sendResponse;
    public TextArea responseContent;
    public HBox windowContainer;
    public Rectangle colorRectangle;
    public ListView identityList;

    private int nestingLevel;
    private Parent wrcNode;
    private WriteResponseControlListener listener;
    private Identifier parentIdentifier;
    private ResponseController parentController;
    private Set<Identity> identities;

    public void setNode(Parent parent) {
        wrcNode = parent;
    }

    public void setParentController(ResponseController rc) {
        initialize();
        parentController = rc;
    }
    public ResponseController getParentController() {
        return parentController;
    }
    public void setParentIdentifier(Identifier parentIdentifier) {
        this.parentIdentifier = parentIdentifier;
    }
    public void registerListener(WriteResponseControlListener listener) {
        this.listener = listener;
    }
    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
        windowContainer.setPadding(new Insets(0,10,0,30 + 35 * nestingLevel)); //top right bottom left
    }
    public void sendResponseClicked(MouseEvent event) {
        listener.createdResponse(wrcNode, this, responseContent.getText(), parentIdentifier, nestingLevel);
    }
    public void initialize(){
        identities=CoreModule.getInstance().getAllIdentities();
        List<String> ids = new ArrayList<String>();
        for(Identity id : identities){
            ids.add(id.getName());
        }
        identityList.setItems(FXCollections.observableList(ids));
        identityList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {
                        if (old_val != null&&old_val!=new_val) {
                            //colorRectangle.setFill(ColorManager.getColorForKey())

                        }
                    }
                });
    }

    public Identity getIdentity(String idName){
        for(Identity id: identities){
            System.out.println("asd");
        }
        return null;
    }


}
