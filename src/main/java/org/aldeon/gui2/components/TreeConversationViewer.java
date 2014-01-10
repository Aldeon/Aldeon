package org.aldeon.gui2.components;

import org.aldeon.gui2.Gui2Utils;

public class TreeConversationViewer extends ConversationViewer {

    public TreeConversationViewer() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/TreeConversationViewer.fxml", this);
    }

    @Override
    public void onRemovedFromScene() {

    }
}
