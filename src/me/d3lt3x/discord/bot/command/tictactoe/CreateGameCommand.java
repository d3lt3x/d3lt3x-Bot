package me.d3lt3x.discord.bot.command.tictactoe;

import me.d3lt3x.discord.bot.command.Command;
import me.d3lt3x.discord.bot.game.TicTacToeGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CreateGameCommand extends Command {


    public CreateGameCommand(String commandLabel, String descriptionLabel, String descriptionValue) {
        super(commandLabel, descriptionLabel, descriptionValue);
    }

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString) {

        if (args.length > 1) {
            getCommandManager().sendSyntax(channel, "ttt");
            return;
        }

        if (!channel.getType().isGuild()) {
            channel.sendMessage("This game doesn't work in private channels yet.").queue();
            return;
        }

        if (message.getMentionedMembers().size() == 1) {

            new TicTacToeGame(user, message.getMentionedMembers().get(0).getUser(), channel);

        } else if (message.getMentionedMembers().size() == 0 && args.length < 1) {

            new TicTacToeGame(user, channel);

        } else {
            System.out.println(args[0]);
            getCommandManager().sendSyntax(channel, "ttt");
        }
    }

}
