package org.aldeon.db;

import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.RsaKeyGen;
import org.aldeon.crypt.Signature;
import org.aldeon.crypt.SignatureImpl;
import org.aldeon.db.queries.Queries;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.ByteSource;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.base64.Base64Codec;
import org.aldeon.utils.base64.MiGBase64Impl;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.helpers.ByteBuffers;
import org.aldeon.utils.helpers.Messages;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
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
import java.util.concurrent.Executor;

public class DbImpl implements Db {

    //TODO: Extract to Settings/Program Constans?
    public static final String DEFAULT_DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";
    public static final String DEFAULT_DRIVER_NAME = "jdbc:hsqldb:file:";
    //TODO: change default path according to detected OS vvv
    public static final String DEFAULT_DB_PATH = "aldeon.db";
    public static final int DEFAULT_QUERY_TIMEOUT = 30;
    public static final String DB_USER =   "sa";
    public static final String DB_PASSWORD = "";

    private Connection connection;
    private String driverClassName;
    private String dbPath;
    private Base64Codec base64Codec;

    public DbImpl()
    {
        this.base64Codec = new MiGBase64Impl();
        this.driverClassName = DEFAULT_DRIVER_CLASS_NAME;
        this.dbPath = DEFAULT_DB_PATH;
        prepareDbConnection();
    }

    public DbImpl(String driverClassName, String dbPath) {
        this.base64Codec = new MiGBase64Impl();
        this.driverClassName = driverClassName;
        this.dbPath = dbPath;
        prepareDbConnection();
    }

    @Override
    public void getMessageById(Identifier msgId, final AsyncCallback<Message> callback) {
        if(msgId == null || connection == null || base64Codec == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.SELECT_MSG_BY_ID);
            setBinaryInPreparedStatement(1, msgId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();


            Identifier msgIdentifier = fromB64(result.getString("msg_id"));
            Identifier authorIdentifier = fromB64(result.getString("author_id"));
            Identifier parentIdentifier = fromB64(result.getString("parent_msg_id"));
            Signature signature = new SignatureImpl(result.getString("msg_sig"), false);
            String content = result.getString("content");

            Message message = Messages.create(msgIdentifier, authorIdentifier, null, content, signature);
            callback.call(message);
        } catch (Exception e) {
            System.err.println("ERROR: Error in getMessageById.");
            e.printStackTrace();
            callback.call(null);
        }
    }

    @Override
    public void insertMessage(Message message, Executor executor) {
        if (message == null || connection == null || base64Codec == null)  {
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.INSERT_MSG);
            setBinaryInPreparedStatement(1, message.getIdentifier(), preparedStatement);
            setBinaryInPreparedStatement(2, message.getSignature(), preparedStatement);
            setBinaryInPreparedStatement(3, message.getAuthorPublicKey(), preparedStatement);
            preparedStatement.setString(4, message.getContent());
            setBinaryInPreparedStatement(5, message.getIdentifier(), preparedStatement);
            setBinaryInPreparedStatement(6, message.getParentMessageIdentifier(), preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR: Error in insertMessage.");
            e.printStackTrace();
            return;
        }

    }

    @Override
    public void deleteMessage(Identifier msgId, Executor executor) {
        if (msgId == null || connection == null || base64Codec == null)  {
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.DELETE_MSG_BY_ID);
            setBinaryInPreparedStatement(1, msgId, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR: Error in deleteMessage.");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, AsyncCallback<Set<Identifier>> callback) {
        if(msgXor == null || connection == null || base64Codec == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.SELECT_MSG_IDS_BY_XOR);
            setBinaryInPreparedStatement(1, msgXor, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            //TODO: is it always only one record?
            // Nope, there WILL be multiple results
            Identifier msgId = fromB64(result.getString("msg_id"));

            // callback.call(msgId);
            callback.call(Collections.EMPTY_SET);
        } catch (Exception e) {
            System.err.println("ERROR: Error in getMessageIdByXor.");
            e.printStackTrace();
            callback.call(null);
            return;
        }
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Message>> callback) {
        callback.call(Collections.EMPTY_SET);
    }

    @Override
    public void getMessageXorById(Identifier msgId, AsyncCallback<Identifier> callback) {
        if(msgId == null || connection == null || base64Codec == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.SELECT_MSG_XOR_BY_ID);
            setBinaryInPreparedStatement(1, msgId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Identifier nodeXor = fromB64(result.getString("node_xor"));

            callback.call(nodeXor);
        } catch (Exception e) {
            System.err.println("ERROR: Error in getMessageXorById.");
            e.printStackTrace();
            callback.call(null);
            return;
        }
    }

    @Override
    public void getMessageIdsByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback) {
        if(parentId == null || connection == null || base64Codec == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.SELECT_MSG_IDS_BY_PARENT_ID);
            setBinaryInPreparedStatement(1, parentId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Set<Identifier> children = new HashSet<>();

            while (result.next()) {
                Identifier msgIdentifier = fromB64(result.getString("msg_id"));
                children.add(msgIdentifier);
            }

            callback.call(children);
        } catch (Exception e) {
            System.err.println("ERROR: Error in getMessageIdsByParentId.");
            e.printStackTrace();
            callback.call(null);
            return;
        }
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback) {
        if(parentId == null || connection == null || base64Codec == null) {
            callback.call(null);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.SELECT_MSG_IDS_AND_XORS_BY_PARENT_ID);
            setBinaryInPreparedStatement(1, parentId, preparedStatement);
            ResultSet result = preparedStatement.executeQuery();

            Map<Identifier, Identifier> idsAndXors = new HashMap<>();

            while (result.next()) {
                Identifier msgIdentifier = fromB64(result.getString("msg_id"));
                Identifier nodeXor = fromB64(result.getString("node_xor"));
                idsAndXors.put(msgIdentifier, nodeXor);
            }

            callback.call(idsAndXors);
        } catch (Exception e) {
            System.err.println("ERROR: Error in getIdsAndXorsByParentId.");
            e.printStackTrace();
            callback.call(null);
            return;
        }
    }

    private void prepareDbConnection() {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: JDBC driver was not properly set.");
            e.printStackTrace();
        }

        boolean dbFileExists = true;

        try {
            File dbFile = new File(dbPath + ".script");
            if (!dbFile.isFile()) {
                dbFileExists = false;
            }

            connection = DriverManager.getConnection(DEFAULT_DRIVER_NAME + dbPath, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERROR: Can not connect to database.");
            e.printStackTrace();
        }

        if (!dbFileExists) {
            createDbSchema();
            insertTestData();
        }
    }

    public void closeDbConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Can not close database connection.");
            e.printStackTrace();
        }
    }

    private void createDbSchema()  {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
            statement.execute(Queries.CREATE_MESSAGE_TABLE);
            statement.execute(Queries.CREATE_MSG_ID_INDEXES);
            statement.execute(Queries.CREATE_MSG_SIGN_INDEXES);
            statement.execute(Queries.CREATE_NODE_XOR_INDEXES);
            statement.execute(Queries.CREATE_CALC_XOR_PROCEDURE);
            statement.execute(Queries.CREATE_MSG_INSERT_TRIGGER);
            statement.execute(Queries.CREATE_MSG_UPDATE_TRIGGER);
            statement.execute(Queries.CREATE_MSG_DELETE_TRIGGER);
        } catch (SQLException e) {
            System.err.println("ERROR: Can not create database schema.");
            e.printStackTrace();
        }
    }

    private void insertTestData() {
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

        insertMessage(topic, e);
        insertMessage(response1, e);
        insertMessage(response11, e);
        insertMessage(otherBranch2, e);

        System.out.println(topic);
    }

    private void setBase64InPreparedStatement(int parameterIndex, ByteSource byteSource, PreparedStatement preparedStatement) throws SQLException {
        try {
            preparedStatement.setString(parameterIndex, base64Codec.encode(byteSource.getByteBuffer()));
        } catch (SQLException e) {
            throw e;
        }
    }

    private void setBinaryInPreparedStatement(int parameterIndex, ByteSource byteSource, PreparedStatement preparedStatement) throws SQLException {
        try {
            ByteBuffer byteBuffer = byteSource.getByteBuffer();

            if (byteBuffer.isReadOnly()) {
                byteBuffer = ByteBuffers.clone(byteBuffer);
            }

            byte[] bytes = byteBuffer.array();
            preparedStatement.setBytes(parameterIndex, bytes);
        } catch (SQLException e) {
            throw e;
        }
    }

    private Identifier fromB64(String b64) {
        try {
            return Identifier.fromByteBuffer(base64Codec.decode(b64), false);
        } catch (ConversionException e) {
            return null;
        }
    }
}
