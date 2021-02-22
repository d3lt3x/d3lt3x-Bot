package me.d3lt3x.discord.bot.command.general;

import me.d3lt3x.discord.bot.command.Command;
import me.d3lt3x.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class ResponseCommand implements Command {

    public static final Map<String, String> RESPONSES = new HashMap<>();

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String rawMessage) {

        if (args[0].equalsIgnoreCase("add")) {
            if (rawMessage.contains("=") && args.length > 1) {
                String[] splitText = rawMessage.replace("add ", "").split("=");
                RESPONSES.put(splitText[0], splitText[1]);
                channel.sendMessage("Response added!").queue();
            } else channel.sendMessage(MessageUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "`+rp add {message}={response}` to add a message response.", false)).queue();

        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length > 1) {
                String response = rawMessage.replace("remove ", "");
                if (RESPONSES.containsKey(response)) {
                    RESPONSES.remove(response);
                    channel.sendMessage("Response removed!").queue();
                } else channel.sendMessage("Response not found!").queue();
            } else channel.sendMessage(MessageUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "`+rp remove {message}` to remove a message response.", false)).queue();
        } else channel.sendMessage(MessageUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "`+rp add/remove` to add or remove a message response.", false)).queue();

    }
}
