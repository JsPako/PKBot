package core.commands.balance;

import core.database.getTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class getBalance {
    public static int fromUser(String USER_ID){
        Connection con = getTable.openConnection();

        try {
            ResultSet rs = getTable.query(con, USER_ID);
            return rs != null ? rs.getInt("balance") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
