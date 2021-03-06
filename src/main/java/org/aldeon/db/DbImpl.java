package org.aldeon.db;

import org.aldeon.crypt.Key;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.db.queries.IdentitiesQueries;
import org.aldeon.db.queries.MessagesQueries;
import org.aldeon.db.queries.UsersQueries;
import org.aldeon.events.Callback;
import org.aldeon.model.*;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.hex.HexCodec;
import org.aldeon.utils.helpers.Callbacks;
import org.aldeon.utils.helpers.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DbImpl implements Db {

    private static final Logger log = LoggerFactory.getLogger(DbImpl.class);

    public static final String DEFAULT_DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";
    public static final String DEFAULT_DRIVER_NAME = "jdbc:hsqldb:file:";
    public static final String DEFAULT_DB_PATH = System.getProperty("user.home") + "/.aldeon/aldeon.db";
    public static final String DEFAULT_DB_PARAMETERS = ";shutdown=true;hsqldb.sqllog=3;hsqldb.log_data=false";
    public static final int DEFAULT_QUERY_TIMEOUT = 30;
    public static final String DB_USER = "sa";
    public static final String DB_PASSWORD = "";
    private static final Codec hex = new HexCodec();

    private Connection connection;
    private String driverClassName;
    private String dbPath;

    public DbImpl() {
        this.driverClassName = DEFAULT_DRIVER_CLASS_NAME;
        this.dbPath = DEFAULT_DB_PATH;
    }

    public DbImpl(String driverClassName, String dbPath) {
        this.driverClassName = driverClassName;
        this.dbPath = dbPath;
    }

    @Override
    public void getMessageById(Identifier msgId, final Callback<Message> callback) {
        if (msgId == null || connection == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSG_BY_ID);
            setIdentifiableInPreparedStatement(1, msgId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                Identifier parentIdentifier = Identifier.fromBytes(result.getBytes("parent_msg_id"), false);

                ByteBuffer pubKeyBuffer = ByteBuffer.wrap(result.getBytes("author_id"));
                RsaKeyGen rsa = new RsaKeyGen();
                Key pubKey = rsa.parsePublicKey(pubKeyBuffer);

                ByteBuffer signatureBuffer = ByteBuffer.wrap(result.getBytes("msg_sign"));
                Signature signature = new Signature(signatureBuffer, false);

                String content = result.getString("content");

                Message message = Messages.create(msgIdentifier, parentIdentifier, pubKey, content, signature);
                callback.call(message);
            } else {
                callback.call(null);
            }
        } catch (Exception e) {
            log.error("Error in getMessageById", e);
            callback.call(null);
        }
    }

    @Override
    public void insertMessage(Message message, Callback<InsertResult> callback) {
        if (message == null || connection == null) {
            callback.call(InsertResult.CRITICAL_ERROR);
            return;
        }

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            PreparedStatement selectMsgPreparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSG_BY_ID);
            setIdentifiableInPreparedStatement(1, message.getIdentifier(), selectMsgPreparedStatement);
            ResultSet selectResult = selectMsgPreparedStatement.executeQuery();

            if (selectResult.next()) {
                connection.setAutoCommit(true);
                callback.call(InsertResult.ALREADY_EXISTS);
                return;
            }

            PreparedStatement preparedStatement;

            if (message.getParentMessageIdentifier().isEmpty()) {
                preparedStatement = connection.prepareStatement(MessagesQueries.INSERT_TOPIC_MSG);
                setIdentifiableInPreparedStatement(1, message.getIdentifier(), preparedStatement);
                setIdentifiableInPreparedStatement(2, message.getSignature(), preparedStatement);
                setIdentifiableInPreparedStatement(3, message.getAuthorPublicKey(), preparedStatement);
                preparedStatement.setString(4, message.getContent());
                setIdentifiableInPreparedStatement(5, message.getIdentifier().xor(Identifier.empty()), preparedStatement);
                setIdentifiableInPreparedStatement(6, message.getParentMessageIdentifier(), preparedStatement);
                setIdentifiableInPreparedStatement(7, message.getIdentifier(), preparedStatement);
            } else {
                preparedStatement = connection.prepareStatement(MessagesQueries.INSERT_MSG);
                setIdentifiableInPreparedStatement(1, message.getIdentifier(), preparedStatement);
                setIdentifiableInPreparedStatement(2, message.getSignature(), preparedStatement);
                setIdentifiableInPreparedStatement(3, message.getAuthorPublicKey(), preparedStatement);
                preparedStatement.setString(4, message.getContent());
                setIdentifiableInPreparedStatement(5, message.getIdentifier(), preparedStatement);
                setIdentifiableInPreparedStatement(6, message.getParentMessageIdentifier(), preparedStatement);
                setIdentifiableInPreparedStatement(7, message.getParentMessageIdentifier(), preparedStatement);
            }

            preparedStatement.executeUpdate();

            connection.commit();

            callback.call(InsertResult.INSERTED);
        } catch (SQLException e) {
            log.error("Error in insertMessage", e);
            callback.call(InsertResult.NO_PARENT);
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("Error in insertMessage", e);
                callback.call(InsertResult.CRITICAL_ERROR);
            }
        }
    }

    @Override
    public void deleteMessage(Identifier msgId, Callback<Boolean> callback) {
        if (msgId == null || connection == null) {
            callback.call(false);
            return;
        }

        try {
            CallableStatement callableStatement = connection.prepareCall(MessagesQueries.CALL_SAFE_REMOVE_BRANCH);
            setIdentifiableInCallableStatement(1, msgId, callableStatement);
            callableStatement.execute();

            callback.call(true);
        } catch (SQLException e) {
            log.error("Error in deleteMessage", e);
            callback.call(false);
        }
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, Callback<Set<Identifier>> callback) {
        if (msgXor == null || connection == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSG_IDS_BY_XOR);
            setIdentifiableInPreparedStatement(1, msgXor, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Set<Identifier> messageIds = new HashSet<>();

            while (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                messageIds.add(msgIdentifier);
            }

            callback.call(messageIds);
        } catch (Exception e) {
            log.error("Error in getMessageIdByXor", e);
            callback.call(null);
        }
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, Callback<Set<Message>> callback) {
        if (parentId == null || connection == null) {
            callback.call(Collections.<Message>emptySet());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSGS_BY_PARENT_ID);
            setIdentifiableInPreparedStatement(1, parentId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Set<Message> messages = new HashSet<>();

            while (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                Identifier parentIdentifier = Identifier.fromBytes(result.getBytes("parent_msg_id"), false);

                ByteBuffer pubKeyBuffer = ByteBuffer.wrap(result.getBytes("author_id"));
                RsaKeyGen rsa = new RsaKeyGen();
                Key pubKey = rsa.parsePublicKey(pubKeyBuffer);

                ByteBuffer signatureBuffer = ByteBuffer.wrap(result.getBytes("msg_sign"));
                Signature signature = new Signature(signatureBuffer, false);

                String content = result.getString("content");

                Message message = Messages.create(msgIdentifier, parentIdentifier, pubKey, content, signature);

                messages.add(message);
            }

            callback.call(messages);
        } catch (Exception e) {
            log.error("Error in getMessagesByParentId", e);
            callback.call(Collections.<Message>emptySet());
        }
    }

    @Override
    public void getMessageXorById(Identifier msgId, Callback<Identifier> callback) {
        if (msgId == null || connection == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSG_XOR_BY_ID);
            setIdentifiableInPreparedStatement(1, msgId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Identifier nodeXor = Identifier.fromBytes(result.getBytes("node_xor"), false);
                callback.call(nodeXor);
            } else {
                callback.call(null);
            }
        } catch (Exception e) {
            log.error("Error in getMessageXorById", e);
            callback.call(null);
        }
    }

    @Override
    public void getMessageIdsByParentId(Identifier parentId, Callback<Set<Identifier>> callback) {
        if(parentId == null || connection == null) {
            callback.call(Collections.<Identifier>emptySet());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSG_IDS_BY_PARENT_ID);
            setIdentifiableInPreparedStatement(1, parentId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Set<Identifier> children = new HashSet<>();

            while (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                children.add(msgIdentifier);
            }

            callback.call(children);
        } catch (Exception e) {
            log.error("Error in getMessageIdsByParentId", e);
            callback.call(Collections.<Identifier>emptySet());
        }
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, Callback<Map<Identifier, Identifier>> callback) {
        if (parentId == null || connection == null) {
            callback.call(Collections.<Identifier, Identifier>emptyMap());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSG_IDS_AND_XORS_BY_PARENT_ID);
            setIdentifiableInPreparedStatement(1, parentId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Map<Identifier, Identifier> idsAndXors = new HashMap<>();

            while (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                Identifier nodeXor = Identifier.fromBytes(result.getBytes("node_xor"), false);
                idsAndXors.put(msgIdentifier, nodeXor);
            }

            callback.call(idsAndXors);
        } catch (Exception e) {
            log.error("Error in getIdsAndXorsByParentId", e);
            callback.call(null);
        }
    }

    @Override
    public void checkAncestry(Identifier descendant, Identifier ancestor, Callback<Boolean> callback) {
        if (descendant == null || ancestor == null || connection == null) {
            callback.call(false);
            return;
        }

        try {
            CallableStatement callableStatement = connection.prepareCall(MessagesQueries.CALL_CHECK_ANCESTRY);
            callableStatement.registerOutParameter(1, Types.BOOLEAN);
            setIdentifiableInCallableStatement(2, descendant, callableStatement);
            setIdentifiableInCallableStatement(3, ancestor, callableStatement);
            callableStatement.executeUpdate();
            Boolean result = callableStatement.getBoolean(1);

            callback.call(result);
        } catch (SQLException e) {
            log.error("Error in checkAncestry", e);
            callback.call(false);
        }
    }

    @Override
    public void getClock(Callback<Long> callback) {
        if (connection == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_CLOCK);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                Long clock = result.getLong("clock");
                callback.call(clock);
            } else {
                log.error("Can not retrieve the result for CLOCK query");
                callback.call(null);
            }
        } catch (Exception e) {
            log.error("ERROR: Error in getMessageIdByXor.", e);
            callback.call(null);
        }
    }

    @Override
    public void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback) {
        if (connection == null) {
            callback.call(Collections.<Message>emptySet());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.SELECT_MSGS_AFTER_CLOCK);
            setIdentifiableInPreparedStatement(1, topic, preparedStatement);
            preparedStatement.setLong(2, clock);
            ResultSet result = preparedStatement.executeQuery();

            Set<Message> messages = new HashSet<>();

            while (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                Identifier parentIdentifier = Identifier.fromBytes(result.getBytes("parent_msg_id"), false);

                ByteBuffer pubKeyBuffer = ByteBuffer.wrap(result.getBytes("author_id"));
                RsaKeyGen rsa = new RsaKeyGen();
                Key pubKey = rsa.parsePublicKey(pubKeyBuffer);

                ByteBuffer signatureBuffer = ByteBuffer.wrap(result.getBytes("msg_sign"));
                Signature signature = new Signature(signatureBuffer, false);

                String content = result.getString("content");

                Message message = Messages.create(msgIdentifier, parentIdentifier, pubKey, content, signature);

                messages.add(message);
            }
            callback.call(messages);
        } catch (Exception e) {
            log.error("ERROR: Error in getMessageIdByXor.", e);
            callback.call(Collections.<Message>emptySet());
        }
    }

    @Override
    public void insertUser(User user, Callback<Boolean> callback) {
        if (user == null || connection == null) {
            callback.call(false);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UsersQueries.INSERT_USER);
            setIdentifiableInPreparedStatement(1, user.getPublicKey(), preparedStatement);
            preparedStatement.setString(2, user.getName());
            preparedStatement.executeUpdate();
            callback.call(true);
        } catch (SQLException e) {
            log.error("Error in insertUser", e);
            callback.call(false);
        }
    }

    @Override
    public void deleteUser(Key publicKey, Callback<Boolean> callback) {
        if (publicKey == null || publicKey.getType() != Key.Type.PUBLIC || connection == null) {
            callback.call(false);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UsersQueries.DELETE_USER_BY_PUBLIC_KEY);
            setIdentifiableInPreparedStatement(1, publicKey, preparedStatement);

            preparedStatement.executeUpdate();
            callback.call(true);
        } catch (SQLException e) {
            log.error("Error in deleteUser", e);
            callback.call(false);
        }
    }

    @Override
    public void getUser(Key publicKey, Callback<User> callback) {
        if (connection == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UsersQueries.SELECT_USER_BY_PUBLIC_KEY);
            setIdentifiableInPreparedStatement(1, publicKey, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                String name = result.getString("name");
                User user = UserImpl.create(name, publicKey);
                callback.call(user);
            } else {
                callback.call(null);
            }
        } catch (Exception e) {
            log.error("Error in getUser.", e);
            callback.call(null);
        }
    }

    @Override
    public void getUsers(Callback<Set<User>> callback) {
        if (connection == null) {
            callback.call(Collections.<User>emptySet());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UsersQueries.SELECT_USERS);
            ResultSet result = preparedStatement.executeQuery();

            Set<User> users = new HashSet<>();
            RsaKeyGen rsa = new RsaKeyGen();

            while (result.next()) {
                ByteBuffer publicKeyBuffer = ByteBuffer.wrap(result.getBytes("public_key"));
                Key publicKey = rsa.parsePublicKey(publicKeyBuffer);

                String name = result.getString("name");
                User user = UserImpl.create(name, publicKey);

                users.add(user);
            }

            callback.call(users);
        } catch (Exception e) {
            log.error("Error in getUsers.", e);
            callback.call(null);
        }
    }

    @Override
    public void insertIdentity(Identity identity, Callback<Boolean> callback) {
        if (identity == null || connection == null) {
            callback.call(false);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IdentitiesQueries.INSERT_IDENTITY);
            setIdentifiableInPreparedStatement(1, identity.getPublicKey(), preparedStatement);
            setIdentifiableInPreparedStatement(2, identity.getPrivateKey(), preparedStatement);
            preparedStatement.setString(3, identity.getName());
            preparedStatement.executeUpdate();
            callback.call(true);
        } catch (SQLException e) {
            log.error("Error in insertIdentity", e);
            callback.call(false);
        }
    }

    @Override
    public void deleteIdentity(Key publicKey, Callback<Boolean> callback) {
        if (publicKey == null || publicKey.getType() != Key.Type.PUBLIC || connection == null) {
            callback.call(false);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IdentitiesQueries.DELETE_IDENTITY_BY_PUBLIC_KEY);
            setIdentifiableInPreparedStatement(1, publicKey, preparedStatement);

            preparedStatement.executeUpdate();
            callback.call(true);
        } catch (SQLException e) {
            log.error("Error in deleteIdentity", e);
            callback.call(false);
        }
    }

    @Override
    public void getIdentity(Key publicKey, Callback<Identity> callback) {
        if (connection == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IdentitiesQueries.SELECT_IDENTITY_BY_PUBLIC_KEY);
            setIdentifiableInPreparedStatement(1, publicKey, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ByteBuffer privateKeyBuffer = ByteBuffer.wrap(result.getBytes("private_key"));
                RsaKeyGen rsa = new RsaKeyGen();
                Key privateKey = rsa.parsePrivateKey(privateKeyBuffer);

                String name = result.getString("name");
                Identity identity = Identity.create(name, publicKey, privateKey);
                callback.call(identity);
            } else {
                callback.call(null);
            }
        } catch (Exception e) {
            log.error("Error in getIdentity.", e);
            callback.call(null);
        }
    }

    @Override
    public void getIdentities(Callback<Set<Identity>> callback) {
        if (connection == null) {
            callback.call(Collections.<Identity>emptySet());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IdentitiesQueries.SELECT_IDENTITIES);
            ResultSet result = preparedStatement.executeQuery();

            Set<Identity> identities = new HashSet<>();
            RsaKeyGen rsa = new RsaKeyGen();

            while (result.next()) {
                ByteBuffer publicKeyBuffer = ByteBuffer.wrap(result.getBytes("public_key"));
                ByteBuffer privateKeyBuffer = ByteBuffer.wrap(result.getBytes("private_key"));

                Key publicKey = rsa.parsePublicKey(publicKeyBuffer);
                Key privateKey = rsa.parsePrivateKey(privateKeyBuffer);
                String name = result.getString("name");
                Identity identity = Identity.create(name, publicKey, privateKey);

                identities.add(identity);
            }

            callback.call(identities);
        } catch (Exception e) {
            log.error("Error in getIdentity.", e);
            callback.call(null);
        }
    }

    private void prepareDbConnection() {

        log.info("Preparing the db connection");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("JDBC driver was not properly set.", e);
            return;
        }

        boolean dbFileExists = true;

        try {
            File dbFile = new File(dbPath + ".script");

            if (!dbFile.isFile()) {
                dbFileExists = false;
            }

            connection = DriverManager.getConnection(DEFAULT_DRIVER_NAME + dbPath + DEFAULT_DB_PARAMETERS, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            log.error("Can not connect to the database.", e);

            try {
                log.info("Trying to delete database lock file.");

                File dbLckFile = new File(dbPath + ".lck");
                if (dbLckFile.isFile()) {
                    log.info("Database lock file detected.");

                    if (!dbLckFile.delete()) {
                        log.error("Can not delete database lock file.");
                    }
                }
                else {
                    log.warn("Database lock file does not exist.");
                }
            } catch (Exception deleteException) {
                log.error("Can not delete database lock file.", deleteException);
            }

            try {
                connection = DriverManager.getConnection(DEFAULT_DRIVER_NAME + dbPath + DEFAULT_DB_PARAMETERS, DB_USER, DB_PASSWORD);
            } catch (SQLException secondConnectException) {
                log.error("Can not connect to the database a second time. Exiting.", secondConnectException);
                System.exit(-1);
            }
        }

        log.info("Connection established");

        if (!dbFileExists) {
            log.info("First-time run: creating the database schema");
            createDbSchema();
        }
    }

    public void closeDbConnection() {
        log.info("Closing the db connection");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
                Statement shutdownStatement = connection.createStatement();
                shutdownStatement.execute("SHUTDOWN");
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Can not close the database connection.", e);
        }
    }

    private void createDbSchema() {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
            statement.execute(UsersQueries.CREATE_USERS_TABLE);
            statement.execute(IdentitiesQueries.CREATE_IDENTITIES_TABLE);
            statement.execute(MessagesQueries.CREATE_MESSAGES_TABLE);
            statement.execute(MessagesQueries.CREATE_MSG_ID_INDEXES);
            statement.execute(MessagesQueries.CREATE_MSG_SIGN_INDEXES);
            statement.execute(MessagesQueries.CREATE_NODE_XOR_INDEXES);
            statement.execute(MessagesQueries.CREATE_TREEWALK_PROCEDURE);
            statement.execute(MessagesQueries.CREATE_REC_DEL_BRANCH_PROCEDURE);
            statement.execute(MessagesQueries.CREATE_REC_DEL_BRANCH_SPEC_PROCEDURE);
            statement.execute(MessagesQueries.CREATE_SAFE_REMOVE_BRANCH_PROCEDURE);
            statement.execute(MessagesQueries.CREATE_CHECK_ANCESTRY_ROCEDURE);
            statement.execute(MessagesQueries.CREATE_MSG_INSERT_TRIGGER);
            insertTestData();
        } catch (SQLException e) {
            log.error("Can not create the database schema", e);
        }
    }

    public void insertTestData() {

        log.info("Inserting test data");
        // Create two users
        KeyGen rsa = new RsaKeyGen();
        KeyGen.KeyPair vader = rsa.generate();
        KeyGen.KeyPair luke = rsa.generate();
        KeyGen.KeyPair random1 = rsa.generate();
        KeyGen.KeyPair random2 = rsa.generate();

        Message topic = Messages.createAndSign(null, vader.publicKey, vader.privateKey, "Obi-Wan never told you what happened to your father.");
        Message otherBranch2 = Messages.createAndSign(topic.getIdentifier(), luke.publicKey, luke.privateKey, "He told me enough!");
        Message response1 = Messages.createAndSign(topic.getIdentifier(), luke.publicKey, luke.privateKey, "He told me you killed him!");
        Message response11 = Messages.createAndSign(response1.getIdentifier(), vader.publicKey, vader.privateKey, "No, I am your father.");
        Message response111 = Messages.createAndSign(response11.getIdentifier(), luke.publicKey, luke.privateKey, "Nooooooooooooooooooo!!!1");
        Message response1111 = Messages.createAndSign(response111.getIdentifier(), random1.publicKey, random1.privateKey, "You gotta love the star wars :D");
        Message response1112 = Messages.createAndSign(response111.getIdentifier(), random2.publicKey, random2.privateKey, "Know that feel, bro...");


        Callback<InsertResult> cb = Callbacks.emptyCallback();
        insertMessage(topic, cb);
        insertMessage(response1, cb);
        insertMessage(response11, cb);
        insertMessage(otherBranch2, cb);
        insertMessage(response111, cb);
        insertMessage(response1111, cb);
        insertMessage(response1112, cb);

        Identity vaderIdentity = Identity.create("Vader", vader.publicKey, vader.privateKey);
        Identity lukeIdentity = Identity.create("Luke", luke.publicKey, luke.privateKey);

        insertIdentity(vaderIdentity, Callbacks.<Boolean>emptyCallback());
        insertIdentity(lukeIdentity, Callbacks.<Boolean>emptyCallback());

        log.info("Inserted topic: " + topic.getIdentifier());
    }

    @Override
    public void dumpMessages(Callback<Set<Message>> callback) {
        if (connection == null) {
            callback.call(Collections.<Message>emptySet());
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.DUMP_MESSAGES);
            ResultSet result = preparedStatement.executeQuery();

            Set<Message> messages = new HashSet<>();

            while (result.next()) {
                Identifier msgIdentifier = Identifier.fromBytes(result.getBytes("msg_id"), false);
                Identifier parentIdentifier = Identifier.fromBytes(result.getBytes("parent_msg_id"), false);

                ByteBuffer pubKeyBuffer = ByteBuffer.wrap(result.getBytes("author_id"));
                RsaKeyGen rsa = new RsaKeyGen();
                Key pubKey = rsa.parsePublicKey(pubKeyBuffer);

                ByteBuffer signatureBuffer = ByteBuffer.wrap(result.getBytes("msg_sign"));
                Signature signature = new Signature(signatureBuffer, false);

                String content = result.getString("content");

                Message message = Messages.create(msgIdentifier, parentIdentifier, pubKey, content, signature);

                messages.add(message);

                ByteBuffer nodeXor = ByteBuffer.wrap(result.getBytes("node_xor"));
                int clock = result.getInt("clock");

                System.out.println(message);
                System.out.println("Node XOR: " + Identifier.fromByteBuffer(nodeXor, true));
                System.out.println("Clock: " + clock);
                System.out.println("");
            }

            callback.call(messages);
        } catch (Exception e) {
            log.error("Error in dumpMessages", e);
            callback.call(Collections.<Message>emptySet());
        }
    }

    private void setIdentifiableInPreparedStatement(int parameterIndex, ByteSource byteSource, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(parameterIndex, hex.encode(byteSource.getByteBuffer()));
    }

    private void setIdentifiableInCallableStatement(int parameterIndex, ByteSource byteSource, CallableStatement callableStatement) throws SQLException {
        callableStatement.setString(parameterIndex, hex.encode(byteSource.getByteBuffer()));
    }

    @Override
    public void start() {
        prepareDbConnection();
    }

    @Override
    public void close() {
        closeDbConnection();
    }
}
