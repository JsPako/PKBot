package core.commands.balance;

import core.database.getTable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class setBalance {
    public static void user(SlashCommandInteractionEvent EVENT , Integer Amount) throws SQLException {
        // Collect the entered values from the slash command
        // userID is the target user that will get their balance set
        // amount is the amount that the balance will be set to
        OptionMapping userOption = EVENT.getOption("user");
        String userID = userOption != null ? userOption.getAsUser().getId() : null;

        // Make a connection to the database
        Connection con = getTable.openConnection();

        // Update the user's database row with the new balance values.
        String updateSQL = String.format("UPDATE Users SET balance = %d WHERE user_id = '%s'",
                Amount, userID);
        Statement stmt = con.createStatement();
        stmt.executeUpdate(updateSQL);

        // Close the database connection
        getTable.closeConnection(con);
    }
}
