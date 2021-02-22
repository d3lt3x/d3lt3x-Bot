package me.d3lt3x.discord.bot.main;

import me.d3lt3x.discord.bot.command.CommandManager;
import me.d3lt3x.discord.bot.command.general.BullyCommand;
import me.d3lt3x.discord.bot.command.general.HelpCommand;
import me.d3lt3x.discord.bot.command.general.ResponseCommand;
import me.d3lt3x.discord.bot.command.general.UnBullyCommand;
import me.d3lt3x.discord.bot.command.tictactoe.CreateGameCommand;
import me.d3lt3x.discord.bot.command.tictactoe.LeaveGameCommand;
import me.d3lt3x.discord.bot.listener.MessageDeleteListener;
import me.d3lt3x.discord.bot.listener.MessageReactionListener;
import me.d3lt3x.discord.bot.listener.MessageReceiveListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BotLauncher {


    public static void main(String[] args) throws LoginException {

        JDABuilder builder = JDABuilder.createDefault("*");
        builder.setActivity(Activity.watching("+help - ver.0.2.3"));
        builder.setStatus(OnlineStatus.ONLINE);

        CommandManager commandManager = registerCommands();
        builder.addEventListeners(
                new MessageReceiveListener(commandManager),
                new MessageReactionListener(),
                new MessageDeleteListener()
        );

        builder.build();
    }

    private static CommandManager registerCommands() {
        return registerCommands(new CommandManager());
    }

    private static CommandManager registerCommands(CommandManager commandManager) {

        commandManager.addCommand("ttt", new CreateGameCommand())
                .addCommand("lv", new LeaveGameCommand())
                .addCommand("rp", new ResponseCommand())
                .addCommand("help", new HelpCommand())
                .addCommand("bully", new BullyCommand())
                .addCommand("unbully", new UnBullyCommand());


        return commandManager;
    }

}
