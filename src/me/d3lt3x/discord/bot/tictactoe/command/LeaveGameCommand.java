package me.d3lt3x.discord.bot.tictactoe.command;

import me.d3lt3x.discord.bot.tictactoe.main.TicTacToeGame;
import me.d3lt3x.discord.bot.tictactoe.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class LeaveGameCommand implements BotCommand {
    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args) {
        if (args.length != 0) {
            channel.sendMessage(EmbedUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "``+lv`` to leave a game.", false)).queue();
            return;
        }

        if (!TicTacToeGame.isInGame(user)) channel.sendMessage("No games to leave.").queue();

        TicTacToeGame game = TicTacToeGame.terminateAbandonedGames(user);
        if (game.getPlayers().size() < 2) channel.sendMessage("You left the game against the bot!").queue();
        else {
            for (User current : game.getPlayers())
                if (!user.equals(current))
                    channel.sendMessage("You left the game against " + current.getAsMention()).queue();
        }
    }
}
