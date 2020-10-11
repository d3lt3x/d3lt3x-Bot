package me.d3lt3x.discord.bot.command.general;

import me.d3lt3x.discord.bot.command.BotCommand;
import me.d3lt3x.discord.bot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class HelpCommand extends BotCommand {

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args) {

        String[] field = {"Use **+ttt** or **+ttt @User**", "Use **+lv**"};
        String[] value = {"to start a TicTacToe game.", "to leave a game."};
        channel.sendMessage(EmbedUtil.messageEmbed("Commands", 0xFFBD00, field, value, false)).queue();
    }
}
