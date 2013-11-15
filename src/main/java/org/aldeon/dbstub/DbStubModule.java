package org.aldeon.dbstub;

import org.aldeon.db.Db;

public class DbStubModule {

    public static Db createInstance() {

        XorManager mgr;

        mgr = new XorManagerImpl();
        mgr = new XorManagerConcurrencyDecorator(mgr);

        return new DbStub(mgr);

    }
}
