package me.d3lt3x.discord.bot.listener;

import me.d3lt3x.discord.bot.command.CommandManager;
import me.d3lt3x.discord.bot.command.general.BullyCommand;
import me.d3lt3x.discord.bot.command.general.ResponseCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MessageReceiveListener extends ListenerAdapter {

    private final CommandManager commandManager;

    public MessageReceiveListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;

        this.commandManager.execute(event.getAuthor(), event.getChannel(), event.getMessage());

        if (ResponseCommand.RESPONSES.containsKey(event.getMessage().getContentRaw().toLowerCase()))
            event.getChannel().sendMessage(ResponseCommand.RESPONSES.get(event.getMessage().getContentRaw())).queue();

        if (BullyCommand.BULLY_LIST.containsKey(event.getAuthor()))
            event.getChannel().sendMessage(BullyCommand.BULLY_LIST.get(event.getAuthor())).queue();
    }

}
