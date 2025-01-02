package site.adwidudev.paraleloserverplugin;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import site.adwidudev.paraleloserverplugin.utils.CommandQueueRunnable;


public class DiscordManager extends ListenerAdapter {
    
    public JDA jda;

    // I cant believe i actually created the class :0
    HashMap<String,Integer> commandsMap = new HashMap<>();
    ArrayList<DiscordCommandsInfo> commands = new ArrayList<DiscordCommandsInfo>();
    
    public void createCommands() {

        /// Here you put the commands you want to create inside the DiscordCommandsInfo object
        // The constructor takes the name of the command, the description and the function that will be called when the command is executed
        AddCommand( new DiscordCommandsInfo("cmdexec", "Execute a command as console in the server", new DiscordCommandsOption[] {new DiscordCommandsOption("command","Command to be executed", OptionType.STRING)}, this::ExecCommand));
        AddCommand( new DiscordCommandsInfo("serverstats", "Returns how the server is going", this::StatsCommand));
    

        // Ugly piece of code that iterates the hashmap and actually registers the commands
        ArrayList<SlashCommandData> commandsData = new ArrayList<SlashCommandData>();
        for (DiscordCommandsInfo obj : commands) {
            Bukkit.getConsoleSender().sendMessage("Registering discord command: "+ ChatColor.BOLD + ChatColor.GOLD + obj.name + ChatColor.RESET);
            if(obj.options.length > 0) {
                if(obj.options.length > 25) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: Too many options for command: " + obj.name);
                    continue;
                }
                else {  

                    SlashCommandData c = Commands.slash(obj.name, obj.description);
                    for (DiscordCommandsOption option : obj.options) {
                        c.addOption(option.type, option.name, option.description);                    
                    }
                    commandsData.add(c);
    
                }
            }
            else {
                commandsData.add(Commands.slash(obj.name, obj.description));
            }
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
    public void StatsCommand(SlashCommandInteractionEvent event) {

        /// Calculate the minecraft servers tps and add them to the message
        
        
        event.deferReply().queue();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Server Stats");
        builder.addField("TPS", "Current TPS: "+Loader.tpsrunnable.getCurrentRoundedTPS(), true);
        builder.addField("Players", "Amount:  "+Bukkit.getServer().getOnlinePlayers().size() + " / " + Bukkit.getServer().getMaxPlayers(), true);
        
        Collection<? extends Player> playersOnline = Bukkit.getServer().getOnlinePlayers();
        String playersOnlineText = "";
        if(playersOnline.size() == 0) {
            playersOnlineText = "No players online";
        }
        else {
            for (Player player :  playersOnline) {
                playersOnlineText += player.getName() + (player.isOp() ? "        (OP)" : "") + "\n";
            }
        }

        builder.addField("Players online", playersOnlineText, false);



        Loader.tpsrunnable.getCurrentTPS();
        
        event.getHook().sendMessageEmbeds(builder.build()).queue(); 
    }
    public void ExecCommand(SlashCommandInteractionEvent event ) {
        
        CommandQueueRunnable.addCommand(event.getOption("command").getAsString());

        
        event.reply("Command queued!").queue();
    }
}
