package core.commands;

import core.commands.balance.getBalance;
import core.commands.balance.pay;
import core.utility.insertUser;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String user = event.getUser().getId();
        String command = event.getName();
        switch (command.toLowerCase()){
            case "ping":
                event.reply("Pong!").queue();
                break;
            case "bal":
                int balance = getBalance.fromUser(user);
                event.reply("Your balance is: " + balance + " shekels.").queue();
                break;
            case "pay":
                OptionMapping userOption = event.getOption("user");
                OptionMapping amountOption = event.getOption("amount");

                assert userOption != null;
                String payUserID = userOption.getAsUser().getId();
                assert amountOption != null;
                Integer payAmount = amountOption.getAsInt();

                pay.user(user, payUserID, payAmount);

                event.reply("You paid <@" + userOption.getAsUser().getId() + "> " + payAmount + " shekels.").queue();
                break;
            case "daily":
                event.reply("Daily Redeemed!").queue();
                break;
            case "coinflip":
                // generate random
                double gamblechance = Math.random();

                // gambling chance - 56 percent edge to the house, 44 to the player, 5 percent chance of jackpot
                double percentchance = 0.56;
                double jackpotchance = 0.95;

                //print to terminal the chance
                System.out.println(gamblechance);

                //pull option from message and convert it to int
                OptionMapping messageOption = event.getOption("amount");
                int gambleamount = messageOption.getAsInt();
                
                //pull balance from db
                int gamblebalance = getBalance.fromUser(user);

                if(gambleamount > gamblebalance)
                {
                    event.reply("Not enough balance to make this action.").queue();
                }
                else
                {
                    if(gamblechance > percentchance)
                    {
                    //win - double
                    gambleamount = gambleamount*2;
                    event.reply("Congratulations, you won " +Integer.toString(gambleamount)).queue();
                    }
                    else if(gamblechance < percentchance)
                    {
                    //fail - lose all
                    gambleamount = 0;
                    event.reply("You lost").queue();
                    }
                    else if(gamblechance > jackpotchance)
                    {
                    //extremely lucky win - multiply by 14
                    gambleamount = gambleamount*14;
                    event.reply("Congratulations, you won the jackpot " +Integer.toString(gambleamount)).queue();
                    }
                }   
                break;

                /* MISSING COMMANDS 
                 * 
                 *  Balance - incomplete
                 *  Pay - incomplete
                 *  Daily - gives user their daily amount of money
                 *  
                 *  admin commands potentially
                 * 
                 *  givemoney - gives money to user
                 *  setmoney - sets users cash
                 *  
                 *  !!FUN!! commands potentially
                 * 
                 *  nuke - costs 90 percent of money in the entire server and restarts everyone back to default amount of cash
                 *  resetall - resets everyones money to default
                 * 
                 */

        }
    }

    // Whenever a user joins a discord server, inserts it to the database.
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        insertUser.onMemberJoin(event);
    }

    // Loops through the all the users in the discord server and inserts (if able) to the database.
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        insertUser.onReady(event);

        // MAX 100 COMMANDS - GUILD ONLY AVAILABLE INSTANTLY USE FOR TESTING ONLY
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("daily","Get your daily payday!"));


        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    // Global Commands - use for production commands can take up to 1 hour to become available.
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping","Check if the bot is responsive."));
        commandData.add(Commands.slash("bal","Displays your balance."));
        OptionData payOption1 = new OptionData(OptionType.USER, "user", "Enter the username of the person you want to pay", true);
        OptionData payOption2 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount to transfer.", true);
        commandData.add(Commands.slash("pay", "Pay an amount of money to a user.").addOptions(payOption1,payOption2));
        //coinflip info - option 1 is the int input
        OptionData coinOption1 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount youd like to gamble for double or nothing.", true);
        commandData.add(Commands.slash("coinflip","enter in the amount you want to gamble for a chance to double or nothing").addOptions(coinOption1));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
