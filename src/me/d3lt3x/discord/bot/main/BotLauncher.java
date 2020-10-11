package me.d3lt3x.discord.bot.main;

import me.d3lt3x.discord.bot.command.*;
import me.d3lt3x.discord.bot.command.general.HelpCommand;
import me.d3lt3x.discord.bot.command.tictactoe.LeaveGameCommand;
import me.d3lt3x.discord.bot.command.tictactoe.TicTacToeCommand;
import me.d3lt3x.discord.bot.listener.MessageListener;
import me.d3lt3x.discord.bot.listener.MessageReactionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BotLauncher {

    private static JDABuilder builder;
    private static JDA jda;
    private static CommandManager commandManager;

    public static void main(String[] args) throws LoginException {

        builder = new JDABuilder("");
        builder.setActivity(Activity.watching("+help - ver.0.1.8"));
        builder.setStatus(OnlineStatus.ONLINE);

        addListeners();
        registerCommands();

        jda = builder.build();
    }

    private static void addListeners() {

        builder.addEventListeners(new MessageListener());
        builder.addEventListeners(new MessageReactionListener());
    }

    private static void registerCommands() {

        commandManager = new CommandManager();
        commandManager.addCommand("ttt", new TicTacToeCommand());
        commandManager.addCommand("lv", new LeaveGameCommand());
        commandManager.addCommand("help", new HelpCommand());
    }

    public static JDA getJda() {
        return jda;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
