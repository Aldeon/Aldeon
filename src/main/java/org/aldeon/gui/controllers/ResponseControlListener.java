package org.aldeon.gui.controllers;

/**
 *
 */
public interface ResponseControlListener {
    void responseClicked(ResponseController rc, String text);
    void responseRespondClicked(ResponseController rc);
    void responseDeleteClicked(ResponseController rc);
}

//or use event loop