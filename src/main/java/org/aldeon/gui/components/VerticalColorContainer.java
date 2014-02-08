package org.aldeon.gui.components;

import org.aldeon.gui.Gui2Utils;

public class VerticalColorContainer extends ColorContainer {

    public VerticalColorContainer() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/VerticalColorContainer.fxml", this);
    }

}
