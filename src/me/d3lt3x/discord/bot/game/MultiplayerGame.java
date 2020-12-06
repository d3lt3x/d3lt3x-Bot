package me.d3lt3x.discord.bot.game;

import net.dv8tion.jda.api.entities.User;

abstract class MultiplayerGame implements Game {

    @Override
    public boolean isInGame(User user) {
        return this.getPlayers().contains(user);
    }
}
