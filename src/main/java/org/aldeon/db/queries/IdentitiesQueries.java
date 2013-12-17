package org.aldeon.db.queries;

import org.aldeon.crypt.rsa.RsaKeyGen;

public class IdentitiesQueries {
    public static final String CREATE_IDENTITIES_TABLE = "CREATE TABLE identities (" +
            "public_key BIT(" + RsaKeyGen.SIZE_BITS + ") NOT NULL," +
            "private_key BIT(" + 2 * RsaKeyGen.SIZE_BITS + ") NOT NULL," +
            "name nvarchar(64) NOT NULL," +
            "PRIMARY KEY (public_key))";

    public static final String INSERT_IDENTITY = "INSERT INTO identities(" +
            "public_key, " +
            "private_key, " +
            "name" +
            ") VALUES (HEXTORAW(?), HEXTORAW(?), ?)";

    public static final String SELECT_IDENTITIES= "SELECT public_key, private_key, name FROM identities";

    public static final String SELECT_IDENTITY_BY_PUBLIC_KEY = "SELECT public_key, private_key, name FROM identities WHERE public_key = HEXTORAW(?)";

    public static final String DELETE_IDENTITY_BY_PUBLIC_KEY = "DELETE FROM identities WHERE public_key = HEXTORAW(?)";
}
