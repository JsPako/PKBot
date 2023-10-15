package core.commands.balance;

import core.database.getTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class getBalance {
    public static int fromUser(String USER_ID) {
        // Connect to the database
        Connection con = getTable.openConnection();

        // Try to look up the user_id in database
        try {
            ResultSet rs = getTable.query(con, USER_ID);
            // Return the user's balance or 0 if not found in the database.
            // Save the user's balance as a local variable to be able to close the connection to the database.
            int balance = rs != null ? rs.getInt("balance") : 0;
            getTable.closeConnection(con);
            return balance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
