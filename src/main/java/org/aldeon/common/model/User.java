package org.aldeon.common.model;

import org.aldeon.common.crypt.Key;

public interface User extends Identifiable {
    Key getPublicKey();
}
