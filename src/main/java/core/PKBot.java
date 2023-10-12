package core;

import core.commands.CommandListener;
import core.events.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;


public class PKBot {

    private final ShardManager shardManager;

    public PKBot() {
        String token = "MTE2MTcwODA3OTczMDEzMTAxNQ.GCK5af.3i3zV7uXvktg5G0AaIKpB7dHNav01mmLvp0mZw";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("with your Mum"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);

        shardManager = builder.build();

        shardManager.addEventListener(new EventListener(),new CommandListener());
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args){
            PKBot bot = new PKBot();
    }
}