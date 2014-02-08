package org.aldeon.gui.various;

import org.aldeon.events.ACB;
import org.aldeon.gui.GuiUtils;

public abstract class FxCallback<T> extends ACB<T> {
    public FxCallback() {
        super(GuiUtils.fxExecutor());
    }
}
