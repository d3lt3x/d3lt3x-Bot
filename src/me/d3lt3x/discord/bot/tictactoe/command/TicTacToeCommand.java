package me.d3lt3x.discord.bot.tictactoe.command;

import me.d3lt3x.discord.bot.tictactoe.main.TicTacToeGame;
import me.d3lt3x.discord.bot.tictactoe.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class TicTacToeCommand implements BotCommand{

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args) {
        if (args.length > 1) {
            channel.sendMessage(EmbedUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "``+ttt`` or ``+ttt @User`` to start a game.", false)).queue();
            return;
        }

        if (message.getMentionedMembers().size() == 1) {

            new TicTacToeGame(user, message.getMentionedMembers().get(0).getUser(), channel);

        } else if (message.getMentionedMembers().size() == 0 && args.length < 1) {

            new TicTacToeGame(user, channel);

        } else {
            channel.sendMessage(EmbedUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "``+ttt`` or ``+ttt @User`` to start a game.", false)).queue();
            return;
        }
    }

}
