package me.d3lt3x.discord.bot.listener;

import me.d3lt3x.discord.bot.game.TicTacToeGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        try {

            User user = event.getUser();

            assert user != null;

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
