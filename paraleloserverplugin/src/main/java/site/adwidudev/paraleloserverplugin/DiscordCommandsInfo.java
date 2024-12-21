package site.adwidudev.paraleloserverplugin;

import java.util.function.Consumer;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DiscordCommandsInfo {
    public DiscordCommandsInfo(String name, String description, Consumer<SlashCommandInteractionEvent> function) {
        this.name = name;
        this.description = description;
        this.function = function;
    }
    public String name;
    public String description;
    public Consumer<SlashCommandInteractionEvent> function;
}
