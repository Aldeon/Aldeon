package org.aldeon.model;

import org.aldeon.crypt.Key;

public interface Profile extends User {
    Key getPrivateKey();
}
