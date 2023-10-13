package core.database;

import java.sql.*;

public class getTable {
    public static ResultSet query(Connection con, String USER_ID) throws SQLException {
        // Query the SQL database and search for the user
        String querySQL = String.format("SELECT * FROM Users WHERE user_id ='%s'", USER_ID);
        Statement stmt = con.createStatement();
        //noinspection SqlSourceToSinkFlow
        ResultSet rs = stmt.executeQuery(querySQL);

        if (rs.next()) {
            return rs;
        } else {
            System.out.println("ID is not existent.");
            return null;
        }
    }

    public static void insert(Connection con, String USER_ID) throws SQLException {
        String insertSQL = "INSERT OR IGNORE INTO Users(user_id, balance, net_gain, total_loss, total_win) VALUES(?, 500, 0, 0, 0)";
        PreparedStatement prepared = con.prepareStatement(insertSQL);
        prepared.setString(1, USER_ID);
        prepared.executeUpdate();
    }

    public static void update(Connection con, String USER_ID, Integer AMOUNT, Boolean RESULT) throws SQLException{
        int balance, net_gain, total_loss, total_win;
        try {
            ResultSet rs = query(con, USER_ID);

            assert rs != null;
            if(RESULT){
                balance = rs.getInt("balance") + AMOUNT;
                net_gain = rs.getInt("net_gain") + AMOUNT;
                total_loss = rs.getInt("total_loss");
                total_win = rs.getInt("total_win") + AMOUNT;
            } else {
                balance = rs.getInt("balance") - AMOUNT;
                net_gain = rs.getInt("net_gain") - AMOUNT;
                total_loss = rs.getInt("total_loss") + AMOUNT;
                total_win = rs.getInt("total_win");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String updateSQL = String.format("UPDATE Users SET balance = %d, net_gain = %d, total_loss = %d, total_win = %d WHERE user_id = '%s'",
                                        balance, net_gain, total_loss, total_win, USER_ID);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(updateSQL);
    }

    public static Connection openConnection() {
        try {
            // Try to make a connection to the sqlite DB
            return DriverManager.getConnection("jdbc:sqlite:db.sqlite");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection con) {
        // Try to close the connection.
        try {
            con.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
