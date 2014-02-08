package org.aldeon.gui.components;


import org.aldeon.gui.GuiUtils;

public class HorizontalColorContainer extends ColorContainer {

    public HorizontalColorContainer(){
        super();
        GuiUtils.loadFXMLandInjectController("/gui/fxml/components/HorizontalColorContainer.fxml", this);
    }
}
