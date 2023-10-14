package core.commands.balance;

import core.database.getTable;

import java.sql.Connection;
import java.sql.SQLException;

public class pay {
    public static void user(String USER_ID, String PAY_ID, Integer AMOUNT){
        Connection con = getTable.openConnection();

        try {
            getTable.update(con, USER_ID, AMOUNT, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            getTable.update(con, PAY_ID, AMOUNT, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        getTable.closeConnection(con);
    }
}
