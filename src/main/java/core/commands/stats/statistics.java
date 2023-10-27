package core.commands.stats;

import core.database.getTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class statistics {
    public static void user(SlashCommandInteractionEvent EVENT, String ID) throws SQLException {
        int Balance, Gain, Loss, Win;

        // Open connection to the database
        Connection con = getTable.openConnection();

        // Collect the result set matching the user's id
        ResultSet rs;
        try {
            rs = getTable.query(con, ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Extract all the necessary values from the result set and save as local variables.
        assert rs != null;
        Balance = rs.getInt("balance");
        Gain = rs.getInt("net_gain");
        Loss = rs.getInt("total_loss");
        Win = rs.getInt("total_win");


        // Close the connection to the database
        getTable.closeConnection(con);

        // Build the embed with title, colour and 4 fields (lines) displaying the statistics
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your Statistics", null);
        eb.setColor(new Color(0xF4C430));
        eb.addField("Your balance is: ", String.valueOf(Balance), false);
        eb.addField("You gained: ", String.valueOf(Gain), false);
        eb.addField("You won in total: ", String.valueOf(Win), true);
        eb.addField("You lost in total: ", String.valueOf(Loss), true);

        // Send the embed back to the user.
        EVENT.replyEmbeds(eb.build()).queue();
    }
}
