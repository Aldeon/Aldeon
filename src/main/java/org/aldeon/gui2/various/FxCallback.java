package org.aldeon.gui2.various;

import org.aldeon.events.ACB;
import org.aldeon.gui2.Gui2Utils;

public abstract class FxCallback<T> extends ACB<T> {
    public FxCallback() {
        super(Gui2Utils.fxExecutor());
    }
}
