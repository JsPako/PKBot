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
import java.util.Random;

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
            case "gamble":
                // generate random
                Random rnd = new Random();

                // generation 50/50 chance - TODO not working
                int gamblechance = rnd.nextInt(1, 2);

                //print to terminal the chance
                System.out.println(gamblechance);

                //pull option from message and convert it to int
                OptionMapping messageOption = event.getOption("amount");
                int gambleamount = messageOption.getAsInt();

                if(gamblechance == 1)
                {
                    //win
                    //double or nothing - TODO fix gamblechance, does not work atm
                    gambleamount = gambleamount*2;
                    event.reply("Congratulations, you won " +Integer.toString(gambleamount)).queue();
                    gamblechance = rnd.nextInt(1,2);
                }
                if(gamblechance == 2)
                {

                    //fail
                    gambleamount = 0;
                    event.reply("You lost").queue();
                    gamblechance = rnd.nextInt(1,2);
                }
                break;
        }
    }

    // MAX 100 COMMANDS - GUILD ONLY AVAILABLE INSTANTLY USE FOR TESTING ONLY
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        //gambling info - option 1 is the int input
        OptionData option1 = new OptionData(OptionType.INTEGER, "amount", "Enter the amount youd like to gamble for double or nothing.", true);
        commandData.add(Commands.slash("gamble","enter in the amount you want to gamble for a chance to double or nothing").addOptions(option1));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }


    // Global Commands - use for production commands can take up to 1 hour to become available.
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping","Check if the bot is responsive."));
        commandData.add(Commands.slash("bal","Displays your balance."));
        commandData.add(Commands.slash("pay","Pay someone"));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
