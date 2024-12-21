package site.adwidudev.paraleloserverplugin;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;


public class DiscordManager extends ListenerAdapter {
    
    public JDA jda;

    // I cant believe i actually created the class :0
    HashMap<String,Integer> commandsMap = new HashMap<>();
    ArrayList<DiscordCommandsInfo> commands = new ArrayList<DiscordCommandsInfo>();
    
    public void createCommands() {

        /// Here you put the commands you want to create inside the DiscordCommandsInfo object
        // The constructor takes the name of the command, the description and the function that will be called when the command is executed
        AddCommand( new DiscordCommandsInfo("Test", "Say Hello world", this::TestCommand));
        AddCommand( new DiscordCommandsInfo("ServerStats", "Returns how the server is going", this::TestCommand));
    

        // Ugly piece of code that iterates the hashmap and actually registers the commands
        ArrayList<SlashCommandData> commandsData = new ArrayList<SlashCommandData>();
        for (DiscordCommandsInfo obj : commands) {
            Bukkit.getConsoleSender().sendMessage("Registering discord command: "+ ChatColor.BOLD + ChatColor.GOLD + obj.name + ChatColor.RESET);
            commandsData.add(Commands.slash(obj.name, obj.description));
        }
        jda.updateCommands().addCommands(commandsData).queue();

    }
    public void AddCommand(DiscordCommandsInfo command) {
        
        commands.add(command);
        commandsMap.put(command.name, commands.size()-1);
    }


    // Ugly i know, but it works lol
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Executes the command that was called
        
        /// this is so bad i had to break it into multiply lines 
        
        commands.get( // From the commands list
            commandsMap.get( // Get the index of the command from the hashmap 
                event.getName() // Using the name of the command
                ).intValue() // Then transform that to an int to get the DiscordCommandsInfo object
                ).function // Then from the returned object get the function
                .accept(event); // And finally call the function with the event as argument
        
    }

    // Bellow are the functions that are called when the command is executed
    public void TestCommand(SlashCommandInteractionEvent event) {
        event.reply("Hello World").queue();
    }
    public void StatsCommand(SlashCommandInteractionEvent event) {
        /// Calculate the minecraft servers tps
        event.deferReply().queue();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Server Stats");
        builder.addField("TPS", "Current TPS: "+Loader.tpsrunnable.getCurrentRoundedTPS(), true);

        Loader.tpsrunnable.getCurrentTPS();
        
        event.getHook().sendMessageEmbeds(builder.build()).queue(); 


    }
}
