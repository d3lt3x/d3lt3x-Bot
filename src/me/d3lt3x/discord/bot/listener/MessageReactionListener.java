package me.d3lt3x.discord.bot.listener;

import me.d3lt3x.discord.bot.game.TicTacToeGame;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactionListener extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        User user = event.getUser();
        assert user != null;

        if (user.isBot())
            return;

        event.retrieveMessage().queue(message -> {
            TicTacToeGame game = TicTacToeGame.getGame(message);
            if (game != null)
                game.pick(user, event.getReaction());
        });

    }
}
