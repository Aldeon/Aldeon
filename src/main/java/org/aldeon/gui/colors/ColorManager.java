package org.aldeon.gui.colors;

import javafx.scene.paint.Color;
import org.aldeon.crypt.Key;

import java.util.HashMap;
import java.util.Map;

public class ColorManager {
    private static Map<Key, Color> colorBase = new HashMap<>();

    public static Color getColorForKey(Key publicKey){
        if(colorBase.containsKey(publicKey)){
            return colorBase.get(publicKey);
        }
        Color clr = ColorGenerator.getColorForSeed(publicKey.hashCode());
        colorBase.put(publicKey,clr);
        return clr;
    }

    public static void removeId(Key publicKey){
        if(colorBase.containsKey(publicKey)) colorBase.remove(publicKey);
    }


}
