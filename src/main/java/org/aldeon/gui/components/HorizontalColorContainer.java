package org.aldeon.gui.components;


import org.aldeon.gui.Gui2Utils;

public class HorizontalColorContainer extends ColorContainer {

    public HorizontalColorContainer(){
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/HorizontalColorContainer.fxml", this);
    }
}
