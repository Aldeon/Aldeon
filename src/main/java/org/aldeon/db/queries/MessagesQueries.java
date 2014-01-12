package org.aldeon.db.queries;

public class MessagesQueries
{
    public static final String CREATE_MESSAGES_TABLE = "CREATE TABLE messages (" +
            "msg_id BIT(256) NOT NULL," +
            "msg_sign BIT(1024) NOT NULL," +
            "author_id BIT(1024) NOT NULL," +
            "content nvarchar(1024) NOT NULL," +
            "node_xor BIT(256) NOT NULL," +
            "parent_msg_id BIT(256) NOT NULL," + // CHECK (SELECT COUNT(*) FROM messages m WHERE m.parent_msg_id = parent_msg_id)," +
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
            "  CALL treewalk(new_row.msg_id, new_row.parent_msg_id);" +
            " END";

    public static final String CREATE_REC_DEL_BRANCH_PROCEDURE = "CREATE PROCEDURE rec_del_branch(IN node_id_p BIT(256))" +
            " SPECIFIC rec_del_branch_impl" +
            " MODIFIES SQL DATA" +
            " SIGNAL SQLSTATE '45000' ";

    public static final String CREATE_REC_DEL_BRANCH_SPEC_PROCEDURE = "ALTER SPECIFIC ROUTINE rec_del_branch_impl " +
            "BEGIN ATOMIC" +
            " for_loop:" +
            " FOR SELECT msg_id FROM messages WHERE parent_msg_id = node_id_p DO"+
            "  CALL rec_del_branch(msg_id);" +
            " END FOR for_loop;" +
            " DELETE FROM messages WHERE msg_id = node_id_p;" +
            "END";

    public static final String CREATE_TREEWALK_PROCEDURE = "CREATE PROCEDURE treewalk(IN xor_p BIT(256), IN node_id_p BIT(256)) " +
            "MODIFIES SQL DATA " +
            "BEGIN ATOMIC" +
            " DECLARE current_node BIT(256);" +
            " SET current_node = node_id_p;" +
            " while_loop: WHILE current_node <> X'FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF' DO" +
            "  UPDATE messages SET node_xor = BITXOR(node_xor, xor_p) WHERE msg_id = current_node;" +
            "  SELECT parent_msg_id INTO current_node FROM messages WHERE msg_id = current_node;" +
            " END WHILE while_loop; " +
            "END";

    public static final String CREATE_SAFE_REMOVE_BRANCH_PROCEDURE = "CREATE PROCEDURE safe_remove_branch(IN node_id_p BIT(256))" +
            "MODIFIES SQL DATA " +
            "BEGIN ATOMIC" +
            " DECLARE branch_xor BIT(256);" +
            " DECLARE branch_parent BIT(256);" +
            " SELECT node_xor, parent_msg_id INTO branch_xor, branch_parent FROM messages WHERE msg_id = node_id_p;" +
            " CALL treewalk(branch_xor, branch_parent);" +
            " CALL rec_del_branch(node_id_p);" +
            "END";

    public static final String CREATE_CHECK_ANCESTRY_ROCEDURE = "CREATE PROCEDURE check_ancestry(OUT result BOOLEAN, IN descendant BIT(256), IN ancestor BIT(256))" +
            "READS SQL DATA " +
            "BEGIN ATOMIC" +
            " DECLARE current_msg_id BIT(256) DEFAULT descend;" +
            " IF descendant = ancestor THEN " +
            "  RETURN FALSE;" +
            " END IF;" +
            " while_loop: WHILE current_msg_id <> X'FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF' DO" +
            "  SELECT parent_msg_id INTO current_msg_id FROM messages WHERE msg_id = current_msg_id;" +
            "  IF current_msg_id = ancestor THEN " +
            "   RETURN TRUE;" +
            "  END IF;" +
            " END WHILE while_loop; " +
            " RETURN FALSE;" +
            "END";

    public static final String CALL_SAFE_REMOVE_BRANCH = "CALL safe_remove_branch(HEXTORAW(?))";

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

    public static final String SELECT_MSG_XOR_BY_ID = "SELECT node_xor FROM messages WHERE msg_id = HEXTORAW(?)";

    public static final String SELECT_MSG_IDS_BY_XOR = "SELECT msg_id FROM messages WHERE node_xor = HEXTORAW(?)";

    public static final String SELECT_MSG_IDS_BY_PARENT_ID = "SELECT msg_id FROM messages WHERE parent_msg_id = HEXTORAW(?)";

    public static final String SELECT_MSG_IDS_AND_XORS_BY_PARENT_ID = "SELECT msg_id, node_xor FROM messages " +
            "WHERE parent_msg_id = HEXTORAW(?)";

    public static final String SELECT_CLOCK = "SELECT ISNULL(MAX(clock), 0) AS clock FROM messages";

    public static final String SELECT_MSGS_AFTER_CLOCK = "SELECT msg_id, msg_sign, author_id, content, parent_msg_id FROM messages WHERE topic_id = HEXTORAW(?) AND clock > ?";

    public static final String CALL_CHECK_ANCESTRY = "{? = CALL checkAncestry(HEXTORAW(?), HEXTORAW(?))}";
}
