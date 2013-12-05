package org.aldeon.dht.miners;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.utils.various.Provider;

public interface DemandWatcher {

    void onUpdate(Callback<Identifier> callback);
    Provider<Integer> getDemand(Identifier topic);

}
