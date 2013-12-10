package org.aldeon.dbstub;

import com.google.inject.Provider;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.db.Db;
import org.aldeon.db.wrappers.DbEventCallerDecorator;
import org.aldeon.events.Callback;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Callbacks;
import org.aldeon.utils.helpers.Messages;

public class DbStubModule implements Provider<Db> {

    private static void addExampleData(Db db) {

        // Create two users

        KeyGen rsa = new RsaKeyGen();

        KeyGen.KeyPair alice = rsa.generate();
        KeyGen.KeyPair bob   = rsa.generate();

        Message topic = Messages.createAndSign(null, alice.publicKey, alice.privateKey, "Some topic");

        Message response1 = Messages.createAndSign(topic.getIdentifier(), bob.publicKey, bob.privateKey, "Response 1");

        Message response11 = Messages.createAndSign(response1.getIdentifier(), alice.publicKey, alice.privateKey, "Response 1.1");

        Message otherBranch2 = Messages.createAndSign(topic.getIdentifier(), alice.publicKey, alice.privateKey, "Response 2");

        Callback<Boolean> cb = Callbacks.emptyCallback();

        db.insertMessage(topic, cb);
        db.insertMessage(response1, cb);
        db.insertMessage(response11, cb);
        db.insertMessage(otherBranch2, cb);

        System.out.println(topic);
    }

    @Override
    public Db get() {

        XorManager mgr;

        // Base XorManager implementation
        mgr = new MapBasedXorManager();
        // Concurrency decorator
        mgr = new XorManagerConcurrencyDecorator(mgr);

        // Actual db
        Db db = new DbStub(mgr);

        // Debug
        addExampleData(db);

        db = new DbEventCallerDecorator(db);

        return db;
    }
}
