package core.commands;

import core.commands.balance.getBalance;
import core.commands.balance.pay;
import core.commands.balance.setBalance;
import core.commands.gamble.coinFlip;
import core.commands.stats.statistics;
import core.utility.insertUser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                event.reply("Your balance is: " + balance + " pisscoins").queue();
                break;
            case "setbal":
                OptionMapping userOption = event.getOption("user");
                OptionMapping amountOption = event.getOption("amount");

                try {
                    assert amountOption != null;
                    setBalance.user(event , amountOption.getAsInt());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                event.reply("You set <@" + (userOption != null ? userOption.getAsUser().getId() : null) + "> to "
                            + amountOption.getAsInt() + " pisscoins").setEphemeral(true).queue();
                break;
            case "pay":
                OptionMapping userPayOption = event.getOption("user");
                OptionMapping amountPayOption = event.getOption("amount");

                pay.user(event, user);
                event.reply("You paid <@" + (userPayOption != null ? userPayOption.getAsUser().getId() : null) + "> "
                            + (amountPayOption != null ? amountPayOption.getAsInt() : 0) + " pisscoins").queue();
                break;
            case "highlow":
                break;
            case "coinflip":
                coinFlip.gamble(event, user);
                break;
            case "resetbal":
                int defAmount = 500;
                try
                {
                    setBalance.user(event, defAmount);
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }
                event.reply("Users balance has been reset").setEphemeral(true).queue();
                break;
            case "stats":
                try {
                    statistics.user(event, user);
                    break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                /* MISSING COMMANDS 
                 *
                 *  Daily - gives user their daily amount of money
                 *  
                 *  admin commands potentially
                 *
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

        commandData.add(Commands.slash("stats","Display your gambling statistics."));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    // Global Commands - use for production commands can take up to 1 hour to become available.
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping","Check if the bot is responsive."));
        commandData.add(Commands.slash("bal","Displays your balance."));
        OptionData payOption1 = new OptionData(OptionType.USER, "user", "Enter the username of the person you want to pay", true);
        OptionData payOption2 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount to transfer.", true).setMaxValue(1000000000).setMinValue(1);
        commandData.add(Commands.slash("pay", "Pay an amount of money to a user.").addOptions(payOption1,payOption2).setGuildOnly(true));
        OptionData coinOption1 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount you'd like to gamble for double or nothing", true).setMaxValue(1000000000).setMinValue(1);
        commandData.add(Commands.slash("coinflip","Enter in the amount you want to gamble for a chance to double or nothing").addOptions(coinOption1));
        OptionData setOption1 = new OptionData(OptionType.USER, "user", "Enter the username of the person's balance you want to set", true);
        OptionData setOption2 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount you want to set the balance to", true).setMaxValue(1000000000).setMinValue(0);
        commandData.add(Commands.slash("setbal","Set user balance").addOptions(setOption1, setOption2).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)).setGuildOnly(true));
        OptionData resetOption1 = new OptionData(OptionType.USER, "user", "Enter the username of the person's balance you want to set", true);
        commandData.add(Commands.slash("resetbal","Reset selected users balance down to 500").addOptions(resetOption1).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)).setGuildOnly(true));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
