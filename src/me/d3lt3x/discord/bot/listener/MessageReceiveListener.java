package me.d3lt3x.discord.bot.listener;

import me.d3lt3x.discord.bot.main.BotLauncher;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MessageReceiveListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        BotLauncher.getCommandManager().execute(event.getAuthor(), event.getChannel(), event.getMessage());
    }
}
