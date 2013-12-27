package org.aldeon.gui2.components;

import org.aldeon.gui2.Gui2Utils;

public class VerticalColorContainer extends ColorContainer {

    public VerticalColorContainer() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/VerticalColorContainer.fxml", this);
    }

}
