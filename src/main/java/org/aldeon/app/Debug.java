package org.aldeon.app;

import org.aldeon.db.DbImpl;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.hex.HexCodec;
import org.aldeon.utils.conversion.ConversionException;

import java.util.Set;

public class Debug {

    public static void main(String[] args) throws ConversionException {

        Codec hex = new HexCodec();


        DbImpl db = new DbImpl();
        db.insertTestData();

        db.getClock(new Callback<Long>() {
            @Override
            public void call(Long val) {
                System.out.println("Clock: " + val);
            }
        });

        Identifier topic = Identifier.fromByteBuffer(hex.decode("1d4c66ce4f24d705f197e76dfc5f35d90fc312ce65ea0ed9f3712813d8256312"), false);
        Long clock = 2l;
        db.getMessagesAfterClock(topic, clock, new Callback<Set<Message>>() {

            @Override
            public void call(Set<Message> val) {

                System.out.println("Returned " + val.size() + " messages");

                for(Message msg: val) {
                    System.out.println("Message: " + msg);
                }
            }

        });

        db.closeDbConnection();
    }
}
