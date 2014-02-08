package org.aldeon.gui.various;

import org.aldeon.events.ACB;
import org.aldeon.gui.Gui2Utils;

public abstract class FxCallback<T> extends ACB<T> {
    public FxCallback() {
        super(Gui2Utils.fxExecutor());
    }
}
