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
import org.aldeon.utils.helpers.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DbImpl implements Db {

    private static final Logger log = LoggerFactory.getLogger(DbImpl.class);

    //TODO: Extract to settings / program constants?
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
    public void insertMessage(Message message) {
        if (message == null || connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement;

            if (message.getParentMessageIdentifier().isEmpty()) {
                preparedStatement = connection.prepareStatement(MessagesQueries.INSERT_TOPIC_MSG);
                setIdentifiableInPreparedStatement(1, message.getIdentifier(), preparedStatement);
                setIdentifiableInPreparedStatement(2, message.getSignature(), preparedStatement);
                setIdentifiableInPreparedStatement(3, message.getAuthorPublicKey(), preparedStatement);
                preparedStatement.setString(4, message.getContent());
                setIdentifiableInPreparedStatement(5, message.getIdentifier(), preparedStatement);
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
        } catch (SQLException e) {
            log.error("Error in insertMessage", e);
        }
    }

    @Override
    public void deleteMessage(Identifier msgId) {
        if (msgId == null || connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(MessagesQueries.DELETE_MSG_BY_ID);
            setIdentifiableInPreparedStatement(1, msgId, preparedStatement);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error in deleteMessage", e);
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
        // TODO: implement
        callback.call(false);
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
                log.error("Could not retrieve the result for CLOCK query");
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
    public void insertUser(User user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteUser(Key publicKey) {
        if (publicKey == null || publicKey.getType() != Key.Type.PUBLIC || connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UsersQueries.DELETE_USER_BY_PUBLIC_KEY);
            setIdentifiableInPreparedStatement(1, publicKey, preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error in deleteUser", e);
        }
    }

    @Override
    public void getUser(Key publicKey) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getUsers() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void insertIdentity(Identity identity) {
        if (identity == null || connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IdentitiesQueries.INSERT_IDENTITY);
            setIdentifiableInPreparedStatement(1, identity.getPublicKey(), preparedStatement);
            setIdentifiableInPreparedStatement(2, identity.getPrivateKey(), preparedStatement);
            preparedStatement.setString(3, identity.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error in insertIdentity", e);
        }
    }

    @Override
    public void deleteIdentity(Key publicKey) {
        if (publicKey == null || publicKey.getType() != Key.Type.PUBLIC || connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IdentitiesQueries.DELETE_IDENTITY_BY_PUBLIC_KEY);
            setIdentifiableInPreparedStatement(1, publicKey, preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error in deleteIdentity", e);
        }
    }

    @Override
    public void getIdentity(Key publicKey) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getIdentities() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void prepareDbConnection() {

        log.info("Preparing the db connection");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("ERROR: JDBC driver was not properly set.", e);
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
            log.error("Could not connect to the database.", e);
            return;
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
            log.error("Could not close the database connection.", e);
        }
    }

    private void createDbSchema() {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
            statement.execute(MessagesQueries.CREATE_MESSAGES_TABLE);
            statement.execute(MessagesQueries.CREATE_MSG_ID_INDEXES);
            statement.execute(MessagesQueries.CREATE_MSG_SIGN_INDEXES);
            statement.execute(MessagesQueries.CREATE_NODE_XOR_INDEXES);
            statement.execute(MessagesQueries.CREATE_CALC_XOR_PROCEDURE);
            statement.execute(MessagesQueries.CREATE_MSG_INSERT_TRIGGER);
            statement.execute(MessagesQueries.CREATE_MSG_UPDATE_TRIGGER);
            statement.execute(MessagesQueries.CREATE_MSG_DELETE_TRIGGER);
            insertTestData();
        } catch (SQLException e) {
            System.err.println("ERROR: Can not create database schema.");
            e.printStackTrace();
        }
    }

    public void insertTestData() {

        log.info("Inserting test data");
        // Create two users
        KeyGen rsa = new RsaKeyGen();
        KeyGen.KeyPair alice = rsa.generate();
        KeyGen.KeyPair bob = rsa.generate();

        Message topic = Messages.createAndSign(null, alice.publicKey, alice.privateKey, "Some topic");
        Message response1 = Messages.createAndSign(topic.getIdentifier(), bob.publicKey, bob.privateKey, "Response 1");
        Message response11 = Messages.createAndSign(response1.getIdentifier(), alice.publicKey, alice.privateKey, "Response 1.1");
        Message otherBranch2 = Messages.createAndSign(topic.getIdentifier(), alice.publicKey, alice.privateKey, "Response 2");

        insertMessage(topic);
        insertMessage(response1);
        insertMessage(response11);
        insertMessage(otherBranch2);

        log.info("Inserted topic: " + topic.getIdentifier());
    }

    private void setIdentifiableInPreparedStatement(int parameterIndex, ByteSource byteSource, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(parameterIndex, hex.encode(byteSource.getByteBuffer()));
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
