package me.d3lt3x.discord.bot.tictactoe.listener;

import me.d3lt3x.discord.bot.tictactoe.main.TicTacToeGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        try {

            User user = event.getUser();
            if (user.isBot()) return;

            Message message = event.retrieveMessage().complete();
            MessageReaction reaction = event.getReaction();

            if (!TicTacToeGame.isGame(message)) return;
            if (!TicTacToeGame.isInGame(message, user)) {
                reaction.removeReaction(user).queue();
                return;
            }

            TicTacToeGame game = TicTacToeGame.getGame(message);
            game.pick(user, reaction);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
