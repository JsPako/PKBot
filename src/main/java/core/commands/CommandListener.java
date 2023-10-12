package core.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

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
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping","Check if the bot is responsive."));
        commandData.add(Commands.slash("bal","Displays your balance."));
        commandData.add(Commands.slash("pay","Pay someone"));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
