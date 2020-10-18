package me.d3lt3x.discord.bot.command;

import me.d3lt3x.discord.bot.util.EmbedUtil;
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

    public void addCommand(String label, Command command) {
        this.commands.put(label, command);
    }

    public void execute(User user, MessageChannel channel, Message message) {

        if (user.isBot())
            return;

        String rawMessage = message.getContentRaw();

        if (!rawMessage.startsWith(this.commandPrefix))
            return;

        String command = rawMessage.replace(this.commandPrefix, "");
        String separator = " ";
        String label = command.split(separator)[0];
        String[] args = (command.contains(separator) ? command.replaceAll(label + " *", "").split(separator) : new String[0]);

        if (this.commands.containsKey(label))
            this.commands.get(label).onCommand(user, channel, message, args);
        else commandNotFound(channel);
    }

    private void commandNotFound(MessageChannel channel) {
        channel.sendMessage(EmbedUtil.messageEmbed("Command not found", 0xFF0042, "**Use:**", "``+help`` for a list of commands.", false)).queue();
    }

}
