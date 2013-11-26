package org.aldeon.model;

import org.aldeon.crypt.Key;

/**
 * Created with IntelliJ IDEA.
 * User: lukas
 * Date: 11.11.13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public interface Profile extends User {
    Key getPrivateKey();
    Key getPublicKey();
    String getName();
}
