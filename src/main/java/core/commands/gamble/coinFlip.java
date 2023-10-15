package core.commands.gamble;

import core.commands.balance.getBalance;
import core.database.getTable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.sql.Connection;
import java.sql.SQLException;

public class coinFlip {
    public static void gamble(SlashCommandInteractionEvent EVENT, String user){
        // generate random
        double gambleChance = Math.random();

        // gambling chance - 56 percent edge to the house, 44 to the player, 5 percent chance of jackpot
        double percentChance = 0.56;
        double jackpotChance = 0.95;

        //print to terminal the chance
        System.out.println("INFO: Coin Flip chance: " + gambleChance);

        //pull option from message and convert it to int
        OptionMapping messageOption = EVENT.getOption("amount");
        assert messageOption != null;
        int gambleAmount = messageOption.getAsInt();

        //pull balance from db
        int balance = getBalance.fromUser(user);

        if(balance < gambleAmount)
        {
            EVENT.reply("Not enough balance to make this action.").queue();
        }
        else
        {
            if(gambleChance > percentChance && gambleChance < jackpotChance)
            {
                //win - double
                gambleAmount = gambleAmount*2;
                Connection con = getTable.openConnection();
                try {
                    getTable.updateGamble(con, user, gambleAmount, true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                int trueBalance = getBalance.fromUser(user);
                getTable.closeConnection(con);
                EVENT.reply("Congratulations, you won " + gambleAmount + " and you now have " + trueBalance + " pisscoins").queue();

            }
            else if(gambleChance < percentChance)
            {

                //fail - lose all
                Connection con = getTable.openConnection();
                try
                {
                    getTable.updateGamble(con, user, gambleAmount, false);
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }
                int trueBalance = getBalance.fromUser(user);
                getTable.closeConnection(con);
                EVENT.reply("You lost and you now have " + trueBalance + " pisscoins").queue();

            }
            else if(gambleChance > jackpotChance)
            {
                //extremely lucky win - multiply by 14
                gambleAmount *= 14;
                Connection con = getTable.openConnection();
                try {
                    getTable.updateGamble(con, user, gambleAmount, true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                int trueBalance = getBalance.fromUser(user);
                getTable.closeConnection(con);
                EVENT.reply("Congratulations, you won the jackpot " + gambleAmount + " and you now have "+ trueBalance + " pisscoins").queue();
            }
        }
    }
}
