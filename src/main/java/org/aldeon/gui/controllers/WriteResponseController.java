package org.aldeon.gui.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aldeon.core.CoreModule;
import org.aldeon.crypt.Key;
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
    public ScrollPane identityScroll;

    private int nestingLevel;
    private Parent wrcNode;
    private WriteResponseControlListener listener;
    private Identifier parentIdentifier;
    private ResponseController parentController;
    private Map<Key,Identity> identities;
    private Identity currentAuthor;

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
        if(currentAuthor==null) sendWarning("You need to choose your identity");
        else listener.createdResponse(wrcNode, this, responseContent.getText(), currentAuthor, parentIdentifier, nestingLevel);
    }
    public void initialize(){
        identities=CoreModule.getInstance().getUserManager().getAllIdentities();
        List<Identity> ids = new ArrayList<Identity>();
        for(Identity id : identities.values()){
            ids.add(id);
        }
        identityList.setItems(FXCollections.observableList(ids));
        identityList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Identity>() {
                    @Override
                    public void changed(ObservableValue<? extends Identity> observableValue, Identity old_id, Identity new_id) {
                        currentAuthor=new_id;
                        colorRectangle.setFill(ColorManager.getColorForKey(new_id.getPublicKey()));
                    }
                });
    }

    public void sendWarning(String message){
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        final Text msg = new Text(message);
        Button ok = new Button("Ok");
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
            }
        });
        Scene scene = new Scene(VBoxBuilder.create().
                children(msg, ok).
                alignment(Pos.CENTER).padding(new Insets(5,5,5,5)).spacing(10).build(),220,80);
        scene.setFill(Color.web("#222222"));
        scene.getStylesheets().add("gui/css/style.css");
        dialogStage.setScene(scene);
        dialogStage.setTitle("Warning");
        dialogStage.show();
    }

    public Identity getIdentity(String idName){
        return null;
    }


}
