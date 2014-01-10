package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.aldeon.core.CoreModule;
import org.aldeon.db.Db;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Callbacks;
import org.aldeon.utils.helpers.Messages;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 *
 */
public class CreateThreadController implements Initializable, WriteResponseControlListener {

    public VBox root;
    public HBox body;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/WriteResponse.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/WriteResponse.fxml").openStream());
        } catch (IOException e) {
        }

        WriteResponseController wrc = loader.getController();
        wrc.setParentIdentifier(Identifier.empty());
        wrc.setNode(parent);
        wrc.registerListener(this);

        body.getChildren().add(parent);
    }

    @Override
    public void createdResponse(Parent wrcNode, WriteResponseController wrc, String responseText, Identity author, Identifier parentIdentifier, int nestingLevel) {
        Message newMsg = Messages.createAndSign(parentIdentifier, author.getPublicKey(), author.getPrivateKey(), responseText);
        CoreModule.getInstance().getStorage().insertMessage(newMsg, Callbacks.<Db.InsertResult>emptyCallback());
        mainController.threadsClicked(null);
    }
}
