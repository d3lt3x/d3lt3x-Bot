package me.d3lt3x.discord.bot.command;

import me.d3lt3x.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    private final String commandPrefix;

    public CommandManager() {
        this("+");
    }

    public CommandManager(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public CommandManager addCommand(String label, Command command) {
        this.commands.put(label.toLowerCase(), command);
        return this;
    }


    public void execute(User user, MessageChannel channel, Message message) {

        if (user.isBot())
            return;

        String rawMessage = MessageUtil.removeIllegalMentions(message);

        if (!rawMessage.startsWith(this.commandPrefix))
            return;

        String command = rawMessage.replace(this.commandPrefix, "");
        String separator = " ";
        String label = command.split(separator)[0].toLowerCase();
        String argsAsString = (command.contains(separator) ? command.replaceFirst(label + " *", "") : "");
        String[] args = argsAsString.split(separator);

        if (this.commands.containsKey(label))
            this.commands.get(label).onCommand(user, channel, message, args, argsAsString);
        else commandNotFound(channel);
    }

    public void commandNotFound(MessageChannel channel) {
        channel.sendMessage(MessageUtil.messageEmbed("Command not found", 0xFF0042, "**Use:**", "`+help` for a list of commands.", false)).queue();
    }

}
