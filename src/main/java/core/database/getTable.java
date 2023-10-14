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
            // Returns the ResultSet (aka the row) with the matching user_id.
            return rs;
        } else {
            // If no matching user_id can be found, print an error and return null.
            System.out.println("ID is not existent.");
            return null;
        }
    }

    public static void insert(Connection con, String USER_ID) throws SQLException {
        // Insert a new user into the Database with predetermined default values.
        String insertSQL = "INSERT OR IGNORE INTO Users(user_id, balance, net_gain, total_loss, total_win) VALUES(?, 500, 0, 0, 0)";
        PreparedStatement prepared = con.prepareStatement(insertSQL);
        // Set the '?' value to be the passed USER_ID.
        prepared.setString(1, USER_ID);
        prepared.executeUpdate();
    }

    public static void update(Connection con, String USER_ID, Integer AMOUNT, Boolean RESULT) throws SQLException{
        // AMOUNT is the POSITIVE number that needs to be updated in the database.
        // RESULT is a function switch takes the AMOUNT and either adds or removes from balance and statistics,
            // TRUE for WON, FALSE for LOSS.
        int balance, net_gain, total_loss, total_win;
        try {
            // Query the database for the user's row
            ResultSet rs = query(con, USER_ID);

            // Make the assumption that the user's row is not null
            assert rs != null;
            if(RESULT){
                // Set the new variables when the user won.
                balance = rs.getInt("balance") + AMOUNT;
                net_gain = rs.getInt("net_gain") + AMOUNT;
                total_loss = rs.getInt("total_loss");
                total_win = rs.getInt("total_win") + AMOUNT;
            } else {
                // Set the new variables when the user lost.
                balance = rs.getInt("balance") - AMOUNT;
                net_gain = rs.getInt("net_gain") - AMOUNT;
                total_loss = rs.getInt("total_loss") + AMOUNT;
                total_win = rs.getInt("total_win");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Update the user's database row with the newly calculated values.
        String updateSQL = String.format("UPDATE Users SET balance = %d, net_gain = %d, total_loss = %d, total_win = %d WHERE user_id = '%s'",
                                        balance, net_gain, total_loss, total_win, USER_ID);
        Statement stmt = con.createStatement();
        //noinspection SqlSourceToSinkFlow
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
