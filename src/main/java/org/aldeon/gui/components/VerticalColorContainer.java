package org.aldeon.gui.components;

import org.aldeon.gui.GuiUtils;

public class VerticalColorContainer extends ColorContainer {

    public VerticalColorContainer() {
        super();
        GuiUtils.loadFXMLandInjectController("/gui/fxml/components/VerticalColorContainer.fxml", this);
    }

}
