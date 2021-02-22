package me.d3lt3x.discord.bot.command.general;

import me.d3lt3x.discord.bot.command.Command;
import me.d3lt3x.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class HelpCommand implements Command {

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString) {

        String[] field = {"Use `+ttt` or `+ttt @User`", "Use `+lv`", "Use `+rp add/remove`", "Use `+bully @User {message}`"};
        String[] value = {"to start a TicTacToe game.", "to leave a game.", "to add or remove a message response.", "to bully a user."};
        channel.sendMessage(MessageUtil.messageEmbed("Commands", 0xFFBD00, field, value, false)).queue();

    }
}
