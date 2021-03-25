package me.d3lt3x.discord.bot.command.tictactoe;

import me.d3lt3x.discord.bot.command.Command;
import me.d3lt3x.discord.bot.game.TicTacToeGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class LeaveGameCommand extends Command {


    public LeaveGameCommand(String commandLabel, String descriptionLabel, String descriptionValue) {
        super(commandLabel, descriptionLabel, descriptionValue);
    }

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString) {

        if (args.length != 0) {
            getCommandManager().sendSyntax(channel, "lv");
            return;
        }

        TicTacToeGame game = TicTacToeGame.getGame(user);

        if (game == null) {
            channel.sendMessage("No games to leave.").queue();
            return;
        }

        game.leaveGame(user);

        if (!game.isMultiPlayer())
            channel.sendMessage("You left the game against the bot!").queue();
        else
            for (User current : game.getPlayers())
                if (!user.equals(current))
                    channel.sendMessage("You left the game against " + current.getAsMention()).queue();
    }
}
