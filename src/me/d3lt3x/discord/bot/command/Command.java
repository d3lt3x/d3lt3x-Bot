package me.d3lt3x.discord.bot.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public abstract class Command {

    private String commandLabel;
    private final Map<String, String> descriptionMap = new HashMap<>();
    private CommandManager commandManager;

    public Command(String commandLabel, String descriptionLabel, String descriptionValue) {
        setDescription(descriptionLabel, descriptionValue);
        setCommandLabel(commandLabel);
    }

    public void setDescription(String descriptionLabel, String descriptionValue) {
        this.descriptionMap.put(descriptionLabel, descriptionValue);
    }

    public Map<String, String> getDescriptionMap() {
        return descriptionMap;
    }

    public String getCommandLabel() {
        return commandLabel;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setCommandLabel(String commandLabel) {
        this.commandLabel = commandLabel;
    }


    public abstract void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString);
}
