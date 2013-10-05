package org.aldeon.app;

import org.aldeon.common.Observer;

import java.util.Random;

/**
 *  Ten obiekt będzie odpytywany przez Jetty (lub inny endpoint) o
 *  wszystkie istotne informacje. Tutaj zaimplementujemy metody
 *  dostępu do wiadomości, ich struktury, autorów, likeów i wszystkich
 *  innych informacji o które mógłby odpytać zewnętrzny peer. Ten
 *  obiekt z kolei może odpytywać bazę lub cokolwiek innego.
 */
public class FooObserver implements Observer {
    @Override
    public int getSomeTestExampleInt() {
        return 42;
    }
}
