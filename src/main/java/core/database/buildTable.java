package core.database;

import java.sql.*;

public class buildTable {
    public static void build() {
        Connection con = null;
        try {
            // Try to make a connection to the sqlite DB
            con = DriverManager.getConnection("jdbc:sqlite:db.sqlite");

            // Runs function that creates a table if one does not exist.
            createTable(con);

            // Creates a mandatory test user and inserts values into the database. If test values already exists ignores.
            try {
                String insertTestSQL = "INSERT INTO Users(user_id, balance, net_gain, total_loss, total_win) VALUES('1aTest', 10000, -40, -50, 10)";
                PreparedStatement pstmt = con.prepareStatement(insertTestSQL); // 'Queues' the prepared insert statement.
                pstmt.executeUpdate(); // Runs insert statement.
            } catch (SQLException ignored){}
        } catch (SQLException e) {
            throw new RuntimeException(e); // Throw exception if a sqlite connection can't be made.
        } finally {
            // Finally if the connection is not NULL (Connection to database is open).
            if (con != null) {
                // Try to close the connection.
                try {
                    con.close();
                } catch (SQLException e) {
                    // Print out any exception the close method throws out.
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // CREATES THE TABLE IF ONE DOES NOT EXIST
    private static void createTable(Connection con) throws SQLException {
        String createSQL = "CREATE TABLE IF NOT EXISTS Users" + // Creates a 'Users' Table if one does not exist.
                "( " + "user_id varchar(255) PRIMARY KEY," + // This column will store unique Discord User IDs as 'user_id'.
                "balance integer," + // Stores the current balance of the user.
                "net_gain integer," + // Stores the net gain statistic of the user (Combined loss and gain).
                "total_loss integer," + // Stores the total loss statistic of the user.
                "total_win integer " + // Stores the total gain statistic of the user.
                "); ";
        Statement stmt = con.createStatement(); // 'Queues' SQL Statement
        stmt.execute(createSQL); // Runs statement
    }
}
