package me.d3lt3x.discord.bot.tictactoe.command;

import me.d3lt3x.discord.bot.tictactoe.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, BotCommand> commands = new HashMap<>();

    public void addCommand(String label, BotCommand command) {
        commands.put(label, command);
    }

    public void execute(User user, MessageChannel channel, Message message) {

        if (user.isBot()) return;

        String rawMessage = message.getContentRaw();

        if (!rawMessage.startsWith("+")) return;

        String command = rawMessage.replace("+", "");
        String label = command.split(" ")[0];
        String[] args = (command.contains(" ") ? command.replaceAll(label + " *", "").split(" ") : new String[0]);

        if (commands.containsKey(label))
            commands.get(label).onCommand(user, channel, message, args);
        else commandNotFound(channel);
    }

    private void commandNotFound(MessageChannel channel) {
        channel.sendMessage(EmbedUtil.messageEmbed("Command not found", 0xFF0042, "**Use:**", "``+help`` for a list of commands.", false)).queue();
    }
}
