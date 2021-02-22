package me.d3lt3x.discord.bot.listener;

import me.d3lt3x.discord.bot.game.TicTacToeGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageDeleteListener extends ListenerAdapter {

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {

        for (Message message : TicTacToeGame.getGameMap().keySet()) {
            if (message.getId().equals(event.getMessageId())) {
                //TicTacToeGame.getGameMap().get(message).refresh();
                event.getChannel().sendMessage("Du Hurensohn").queue();
            }
        }
    }


}
