package me.d3lt3x.discord.bot.listener;

import me.d3lt3x.discord.bot.command.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MessageReceiveListener extends ListenerAdapter {

    private final CommandManager commandManager;

    public MessageReceiveListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        this.commandManager.execute(event.getAuthor(), event.getChannel(), event.getMessage());
    }
}
