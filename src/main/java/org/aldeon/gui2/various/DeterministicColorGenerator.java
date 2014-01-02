package org.aldeon.gui2.various;

import javafx.scene.paint.Color;
import org.aldeon.crypt.Key;
import org.aldeon.model.Message;
import org.aldeon.model.User;

public class DeterministicColorGenerator {

    public static Color get(int seed){
        seed = Math.abs(seed);
        double h,s,b;
        h = ((seed * 0.12345) % 1.0) * 360;
        s = (seed * 0.23456) % 0.2 + 0.8;
        b = (seed * 0.34567) % 0.2 + 0.8;
        return Color.hsb(h,s,b);
    }

    public static Color get(Message message) {
        return get(message.getAuthorPublicKey());
    }

    public static Color get(Key key) {
        return get(key.hashCode());
    }

    public static Color get(User user){
        return get(user.getPublicKey());
    }
}
