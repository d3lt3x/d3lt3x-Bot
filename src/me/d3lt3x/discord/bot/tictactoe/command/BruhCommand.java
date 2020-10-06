package me.d3lt3x.discord.bot.tictactoe.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class BruhCommand implements BotCommand{
    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args) {
        channel.sendMessage("bruh").queue();
    }
}
