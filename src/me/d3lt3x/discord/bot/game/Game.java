package me.d3lt3x.discord.bot.game;

import net.dv8tion.jda.api.entities.User;

import java.util.List;

public interface Game {

    boolean isInGame(User user);

    List<User> getPlayers();

    boolean isMultiPlayer();

    void stop();

    void leaveGame(User user);
}
