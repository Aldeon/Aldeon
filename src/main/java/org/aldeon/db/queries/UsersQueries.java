package org.aldeon.db.queries;

import org.aldeon.crypt.rsa.RsaKeyGen;

public class UsersQueries {
    public static final String CREATE_USERS_TABLE = "CREATE TABLE users (" +
            "public_key BIT(" + RsaKeyGen.SIZE_BITS + ") NOT NULL," +
            "name nvarchar(64) NOT NULL," +
            "PRIMARY KEY (public_key))";

    public static final String INSERT_USER = "INSERT INTO users(" +
            "public_key, " +
            "name" +
            ") VALUES (HEXTORAW(?), ?)";

    public static final String SELECT_USERS = "SELECT public_key, name FROM users";

    public static final String SELECT_USER_BY_PUBLIC_KEY = "SELECT public_key, name FROM users WHERE public_key = HEXTORAW(?)";

    public static final String DELETE_USER_BY_PUBLIC_KEY = "DELETE FROM users WHERE public_key = HEXTORAW(?)";
}
