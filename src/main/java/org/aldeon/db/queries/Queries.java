package org.aldeon.db.queries;

/**
 * Created with IntelliJ IDEA.
 * User: lukas
 * Date: 08.11.13
 * Time: 12:40
 * To change this template use File | Settings | File Templates.
 */
public class Queries {
    public static final String CREATE_MESSAGE_TABLE = "CREATE TABLE messages (" +
                                                      "msg_id nvarchar(344) NOT NULL," +
                                                      "msg_sign nvarchar(1368) NOT NULL," +
                                                      "author_id nvarchar(344) NOT NULL," +
                                                      "content nvarchar(1024) NOT NULL," +
                                                      "node_xor nvarchar(344) NOT NULL," +
                                                      "parent_msg_id nvarchar(344) NOT NULL," +
                                                      "PRIMARY KEY (msg_id)," +
                                                      "UNIQUE (msg_sign)," +
                                                      "FOREIGN KEY (parent_msg_id) REFERENCES messages(msg_id)" +
                                                      ")";

    public static final String CREATE_MSG_ID_INDEXES = "CREATE INDEX msg_id_index ON messages(msg_id)";

    public static final String CREATE_MSG_SIGN_INDEXES = "CREATE INDEX msg_sign_index ON messages(msg_sign);";

    public static final String CREATE_NODE_XOR_INDEXES = "CREATE INDEX node_xor_index ON messages(node_xor)";

    public static final String SELECT_MSG_BY_ID = "SELECT * FROM messages WHERE msg_id = ?";

    public static final String INSERT_MSG = "INSERT INTO messages(" +
                                            "msg_id, " +
                                            "msg_sign," +
                                            "author_id," +
                                            "content," +
                                            "node_xor," +
                                            "parent_msg_id" +
                                            ") VALUES (?, ?, ?, ?, ?, ?)";

    public static final String DELETE_MSG_BY_ID = "DELETE FROM messages WHERE msg_id = ?";

    public static final String SELECT_MSG_XOR_BY_ID = "SELECT node_xor FROM messages WHERE msg_id = ?";

    public static final String SELECT_MSG_IDS_BY_XOR = "SELECT msg_id FROM messages WHERE node_xor = ?";

    public static final String SELECT_MSG_IDS_BY_PARENT_ID = "SELECT msg_id FROM messages WHERE parent_msg_id = ?";

    public static final String SELECT_MSG_IDS_AND_XORS_BY_PARENT_ID = "SELECT msg_id, node_xor FROM messages " +
                                                                      "WHERE parent_msg_id = ?";

}
