package site.adwidudev.paraleloserverplugin;


import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import site.adwidudev.paraleloserverplugin.utils.TPSRunnable;

public class Loader extends JavaPlugin{
    private final String token = "OTMzMDUwNzMzMzc5NjE2ODE4.GPYam1.cUycLCzagaAvp96QUFdPRK5HrIJenqg1NS33a4";

    public static TPSRunnable tpsrunnable = new TPSRunnable();
    JDA jda;
    @Override
    public void onEnable() {
        
        // Creates the listener as an object
        DiscordManager commandRegisterer = new DiscordManager();

        // Creates the JDA gateway and registers the listener
        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        builder.setActivity(Activity.watching("you"));
        builder.addEventListeners(commandRegisterer);
        jda = builder.build();

        // Creates the command list
        commandRegisterer.jda = jda;
        commandRegisterer.createCommands();

    }
}
