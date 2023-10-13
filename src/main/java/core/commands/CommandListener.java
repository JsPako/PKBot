package core.commands;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        switch (command.toLowerCase()){
            case "ping":
                event.reply("Pong!").queue();
                break;
            case "bal":
                event.reply("Your balance is 1,000,000 bananas.").queue();
                break;
            case "pay":
                event.reply("You paid shekels").queue();
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

    // MAX 100 COMMANDS - GUILD ONLY AVAILABLE INSTANTLY USE FOR TESTING ONLY
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }


    // Global Commands - use for production commands can take up to 1 hour to become available.
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping","Check if the bot is responsive."));
        commandData.add(Commands.slash("bal","Displays your balance."));
        commandData.add(Commands.slash("pay","Pay someone"));
        //coinflip info - option 1 is the int input
        OptionData option1 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount youd like to gamble for double or nothing.", true);
        commandData.add(Commands.slash("coinflip","enter in the amount you want to gamble for a chance to double or nothing").addOptions(option1));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
