package org.aldeon.gui2.components;


import org.aldeon.gui2.Gui2Utils;

public class HorizontalColorContainer extends ColorContainer {

    public HorizontalColorContainer(){
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/HorizontalColorContainer.fxml", this);
    }
}
