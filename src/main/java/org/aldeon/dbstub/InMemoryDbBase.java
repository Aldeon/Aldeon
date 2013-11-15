package org.aldeon.dbstub;


import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the internal db structure and insert/remove mechanisms
 */
public abstract class InMemoryDbBase {

    protected static final int PUT_SUCCESS = 1;
    protected static final int PUT_INVALID_MSG = 2;
    protected static final int PUT_NO_PARENT = 3;
    protected static final int PUT_MSG_EXISTS = 4;
    protected static final int PUT_ID_CONFLICT = 5;

    protected Map<Identifier, Message> messages;
    protected XorManager xorManager;

    protected InMemoryDbBase() {
        messages = new HashMap<>();
        xorManager = new XorManager();
    }

    protected int put(Message message) {
        // Check if the identifier exists in the database
        if(messages.containsKey(message.getIdentifier())) {

            // Identifier used.
            Message inDb = messages.get(message.getIdentifier());

        } else {

        }

        return 0;
    }

}
