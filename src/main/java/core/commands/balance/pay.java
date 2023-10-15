package core.commands.balance;

import core.database.getTable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.sql.Connection;
import java.sql.SQLException;

public class pay {
    public static void user(SlashCommandInteractionEvent EVENT, String USER_ID) {
        // Collect the entered values from the slash command
        // payUserID is the target user that will get paid
        // payAmount is the amount that will get paid
        OptionMapping userOption = EVENT.getOption("user");
        OptionMapping amountOption = EVENT.getOption("amount");
        String payUserID = userOption != null ? userOption.getAsUser().getId() : null;
        int payAmount = amountOption != null ? amountOption.getAsInt() : 1;

        // Get the balance of the user and check if they have enough to pay the target amount.
        int userBalance = getBalance.fromUser(USER_ID);
        if (userBalance < payAmount) {
            EVENT.reply("❌ You don't have enough pisscoins to do this").queue();
            return;
        }

        // Make a connection to the database
        Connection con = getTable.openConnection();

        // Try to remove the entered amount from the user's balance
        try {
            getTable.updateBalance(con, USER_ID, payAmount, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Try to add the entered amount to the target user's balance.
        try {
            getTable.updateBalance(con, payUserID, payAmount, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Close the connection to database.
        getTable.closeConnection(con);
    }
}
