package org.aldeon.model;

import org.aldeon.crypt.Key;

public interface Identity extends User {
    Key getPrivateKey();
}
