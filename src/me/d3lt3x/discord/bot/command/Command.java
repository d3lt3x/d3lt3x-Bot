package me.d3lt3x.discord.bot.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface Command {

    void onCommand(User user, MessageChannel channel, Message message, String[] args);


}
