package org.aldeon.db.queries;

public class Queries {
    public static final String CREATE_MESSAGE_TABLE = "CREATE TABLE messages (" +
            "msg_id BIT(256) NOT NULL," +
            "msg_sign BIT(1024) NOT NULL," +
            "author_id BIT(1024) NOT NULL," +
            "content nvarchar(1024) NOT NULL," +
            "node_xor BIT(256) NOT NULL," +
            "parent_msg_id BIT(256) NOT NULL," +
            "topic_id BIT(256) NOT NULL," +
            "clock BIGINT NOT NULL, " +
            "PRIMARY KEY (msg_id)," +
            "UNIQUE (msg_sign))";

    //generated by database engine
    public static final String CREATE_MSG_ID_INDEXES = "CREATE INDEX msg_id_index ON messages(msg_id)";

    //generated by database engine
    public static final String CREATE_MSG_SIGN_INDEXES = "CREATE INDEX msg_sign_index ON messages(msg_sign)";

    public static final String CREATE_NODE_XOR_INDEXES = "CREATE INDEX node_xor_index ON messages(node_xor)";

    public static final String CREATE_MSG_INSERT_TRIGGER = "CREATE TRIGGER msg_insert_trigger AFTER INSERT ON messages" +
            " REFERENCING NEW AS new_row" +
            " FOR EACH ROW" +
            " BEGIN ATOMIC" +
            "  UPDATE messages SET node_xor = BITXOR(new_row.msg_id, (SELECT node_xor FROM messages WHERE msg_id = new_row.parent_msg_id)) WHERE msg_id = new_row.parent_msg_id;" +
            " END";

    public static final String CREATE_MSG_UPDATE_TRIGGER = "CREATE TRIGGER msg_update_trigger AFTER INSERT ON messages" +
            " REFERENCING NEW AS new_row" +
            " FOR EACH ROW" +
            " BEGIN ATOMIC" +
            "  UPDATE messages SET node_xor = BITXOR(new_row.msg_id, (SELECT node_xor FROM messages WHERE msg_id = new_row.parent_msg_id)) WHERE msg_id = new_row.parent_msg_id;" +
            " END";

    public static final String CREATE_MSG_DELETE_TRIGGER = "CREATE TRIGGER msg_delete_trigger AFTER INSERT ON messages" +
            " REFERENCING NEW AS new_row" +
            " FOR EACH ROW" +
            " BEGIN ATOMIC" +
            //"  CALL CALC_XOR(new_row.parent_msg_id);" +
            "  UPDATE messages SET node_xor = BITXOR(new_row.msg_id, (SELECT node_xor FROM messages WHERE msg_id = new_row.parent_msg_id)) WHERE msg_id = new_row.parent_msg_id;" +
            " END";

    public static final String CREATE_CALC_XOR_PROCEDURE = "CREATE PROCEDURE" +
            "  CALC_XOR(IN parent_msg_id_p BIT(256))" +
            "  MODIFIES SQL DATA" +
            " BEGIN ATOMIC" +
            "  DECLARE calculated_xor BIT(256) DEFAULT X'00';" +
            "  for_loop: FOR SELECT node_xor FROM messages WHERE parent_msg_id = parent_msg_id_p DO" +
            "   SET calculated_xor = BITXOR(calculated_xor, node_xor);" +
            "  END FOR for_loop;" +
            "  UPDATE messages SET node_xor = calculated_xor WHERE msg_id = parent_msg_id_p;" +
            " END";

    public static final String SELECT_MSG_BY_ID = "SELECT msg_id, msg_sign, author_id, content, parent_msg_id FROM messages WHERE msg_id = HEXTORAW(?)";

    public static final String SELECT_MSGS_BY_PARENT_ID = "SELECT msg_id, msg_sign, author_id, content, parent_msg_id FROM messages WHERE parent_msg_id = HEXTORAW(?)";

    public static final String INSERT_MSG = "INSERT INTO messages(" +
            "msg_id, " +
            "msg_sign," +
            "author_id," +
            "content," +
            "node_xor," +
            "parent_msg_id," +
            "topic_id," +
            "clock" +
            ") VALUES (HEXTORAW(?), HEXTORAW(?), HEXTORAW(?), ?, HEXTORAW(?), HEXTORAW(?), (SELECT topic_id FROM messages WHERE msg_id = HEXTORAW(?)), (SELECT ISNULL(MAX(clock) + 1, 0) FROM messages) )";

    public static final String INSERT_TOPIC_MSG = "INSERT INTO messages(" +
            "msg_id, " +
            "msg_sign," +
            "author_id," +
            "content," +
            "node_xor," +
            "parent_msg_id," +
            "topic_id," +
            "clock" +
            ") VALUES (HEXTORAW(?), HEXTORAW(?), HEXTORAW(?), ?, HEXTORAW(?), HEXTORAW(?), HEXTORAW(?), (SELECT ISNULL(MAX(clock) + 1, 0) FROM messages) )";

    public static final String DELETE_MSG_BY_ID = "DELETE FROM messages WHERE msg_id = HEXTORAW(?)";

    public static final String SELECT_MSG_XOR_BY_ID = "SELECT node_xor FROM messages WHERE msg_id = HEXTORAW(?)";

    public static final String SELECT_MSG_IDS_BY_XOR = "SELECT msg_id FROM messages WHERE node_xor = HEXTORAW(?)";

    public static final String SELECT_MSG_IDS_BY_PARENT_ID = "SELECT msg_id FROM messages WHERE parent_msg_id = HEXTORAW(?)";

    public static final String SELECT_MSG_IDS_AND_XORS_BY_PARENT_ID = "SELECT msg_id, node_xor FROM messages " +
            "WHERE parent_msg_id = HEXTORAW(?)";

    public static final String SELECT_CLOCK = "SELECT ISNULL(MAX(clock), 0) AS clock FROM messages";
    public static final String SELECT_MSGS_AFTER_CLOCK = "SELECT msg_id, msg_sign, author_id, content, parent_msg_id FROM messages WHERE topic_id = HEXTORAW(?) AND clock > ?";
}
