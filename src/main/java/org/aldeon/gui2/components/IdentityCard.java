package org.aldeon.gui2.components;

import org.aldeon.gui2.Gui2Utils;

public class IdentityCard {

    public IdentityCard() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/IdentityCard.fxml", this);
    }
}
