package site.adwidudev.paraleloserverplugin;

import java.util.function.Consumer;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DiscordCommandsInfo {
 
    public DiscordCommandsInfo(String name, String description, Consumer<SlashCommandInteractionEvent> function) {
        this.name = name;
        this.description = description;
        this.function = function;
        options = new DiscordCommandsOption[0];
    }
    public DiscordCommandsInfo(String name, String description, DiscordCommandsOption[] options, Consumer<SlashCommandInteractionEvent> function) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.function = function;
    }
    public String name;
    public String description;
    public DiscordCommandsOption[] options;
    public Consumer<SlashCommandInteractionEvent> function;
}
