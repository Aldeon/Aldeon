package org.aldeon.dbstub;

import com.google.inject.Provider;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.RsaKeyGen;
import org.aldeon.db.Db;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Messages;

import java.util.concurrent.Executor;

public class DbStubModule implements Provider<Db> {

    private static void addExampleData(Db db) {

        // Synchronous executor
        Executor e = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };


        // Create two users

        KeyGen rsa = new RsaKeyGen();

        KeyGen.KeyPair alice = rsa.generate();
        KeyGen.KeyPair bob   = rsa.generate();

        Message topic = Messages.createAndSign(null, alice.publicKey, alice.privateKey, "Some topic");

        Message response1 = Messages.createAndSign(topic.getIdentifier(), bob.publicKey, bob.privateKey, "Response 1");

        Message response11 = Messages.createAndSign(response1.getIdentifier(), alice.publicKey, alice.privateKey, "Response 1.1");

        Message otherBranch2 = Messages.createAndSign(topic.getIdentifier(), alice.publicKey, alice.privateKey, "Response 2");

        db.insertMessage(topic, e);
        db.insertMessage(response1, e);
        db.insertMessage(response11, e);
        db.insertMessage(otherBranch2, e);

        System.out.println(topic);
    }

    @Override
    public Db get() {

        XorManager mgr;

        // Base XorManager implementation
        mgr = new XorManagerImpl();
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
