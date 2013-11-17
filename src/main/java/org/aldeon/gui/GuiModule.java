package org.aldeon.gui;

import javafx.application.Application;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.IdentityAddedEvent;
import org.aldeon.core.events.IdentityRemovedEvent;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.core.events.MessageRemovedEvent;
import org.aldeon.core.events.TopicAddedEvent;
import org.aldeon.core.events.TopicRemovedEvent;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.events.CallbackAndExecutor;
import org.aldeon.gui.callbacks.IdentityAddedCallback;
import org.aldeon.gui.callbacks.IdentityRemovedCallback;
import org.aldeon.gui.callbacks.MessageAddedCallback;
import org.aldeon.gui.callbacks.MessageRemovedCallback;
import org.aldeon.gui.callbacks.TopicAddedCallback;
import org.aldeon.gui.callbacks.TopicRemovedCallback;

import java.util.concurrent.Executor;

public class GuiModule {

    public static void launch() {
        
        setupCallbacks(CoreModule.getInstance());
        
        Application.launch(GUIController.class, new String[0]);
    }

    /**
     * Register the UI-related callbacks
     * @param core
     */
    private static void setupCallbacks(Core core) {

        // For client-related tasks we use a clientSideExecutor
        Executor cse = core.clientSideExecutor();

        /*
            Here go all the callbacks we need
         */

        core.getEventLoop().assign(TopicAddedEvent.class,       async(new TopicAddedCallback(), cse));
        core.getEventLoop().assign(TopicRemovedEvent.class,     async(new TopicRemovedCallback(), cse));
        core.getEventLoop().assign(MessageAddedEvent.class,     async(new MessageAddedCallback(), cse));
        core.getEventLoop().assign(MessageRemovedEvent.class,   async(new MessageRemovedCallback(), cse));
        core.getEventLoop().assign(IdentityAddedEvent.class,    async(new IdentityAddedCallback(), cse));
        core.getEventLoop().assign(IdentityRemovedEvent.class,  async(new IdentityRemovedCallback(), cse));

        // lots of other shizzle-wizzle
    }

    private static <T> AsyncCallback<T> async(Callback<T> callback, Executor executor) {
        return new CallbackAndExecutor<T>(callback, executor);
    }
}
