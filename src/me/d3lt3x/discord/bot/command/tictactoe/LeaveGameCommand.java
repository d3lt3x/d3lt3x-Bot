package me.d3lt3x.discord.bot.command.tictactoe;

import me.d3lt3x.discord.bot.command.Command;
import me.d3lt3x.discord.bot.game.TicTacToeGame;
import me.d3lt3x.discord.bot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class LeaveGameCommand implements Command {

    
    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args) {

        if (args.length != 0) {
            channel.sendMessage(EmbedUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "``+lv`` to leave a game.", false)).queue();
            return;
        }

        TicTacToeGame game = TicTacToeGame.getGame(user);

        if (game == null) {
            channel.sendMessage("No games to leave.").queue();
            return;
        }
        game.leaveGame(user);

        if (game.isMultiPlayer())
            channel.sendMessage("You left the game against the bot!").queue();
        else
            for (User current : game.getPlayers())
                if (!user.equals(current))
                    channel.sendMessage("You left the game against " + current.getAsMention()).queue();
    }
}
