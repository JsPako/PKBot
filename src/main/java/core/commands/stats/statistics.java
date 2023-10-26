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

        Connection con = getTable.openConnection();

        ResultSet rs;
        try {
            rs = getTable.query(con, ID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        assert rs != null;
        Balance = rs.getInt("balance");
        Gain = rs.getInt("net_gain");
        Loss = rs.getInt("total_loss");
        Win = rs.getInt("total_win");

        getTable.closeConnection(con);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your Statistics", null);
        eb.setColor(new Color(0xF4C430));
        eb.addField("Your balance is: ", String.valueOf(Balance), false);
        eb.addField("You gained: ", String.valueOf(Gain), false);
        eb.addField("You won in total: ", String.valueOf(Win), false);
        eb.addField("You lost in total: ", String.valueOf(Loss), false);

        EVENT.getChannel().sendMessageEmbeds(eb.build()).queue();
    }
}
