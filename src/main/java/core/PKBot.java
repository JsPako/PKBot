package core;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class PKBot {
    public static void main(String[] arguments) throws Exception {
        JDA api = JDABuilder.createDefault("token").build();
    }
}