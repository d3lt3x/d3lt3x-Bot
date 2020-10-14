package me.d3lt3x.discord.bot.main;

import me.d3lt3x.discord.bot.command.CommandManager;
import me.d3lt3x.discord.bot.command.general.HelpCommand;
import me.d3lt3x.discord.bot.command.tictactoe.CreateGameCommand;
import me.d3lt3x.discord.bot.command.tictactoe.LeaveGameCommand;
import me.d3lt3x.discord.bot.listener.MessageReactionListener;
import me.d3lt3x.discord.bot.listener.MessageReceiveListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BotLauncher {


    public static void main(String[] args) throws LoginException {

        JDABuilder builder = JDABuilder.createDefault("NzIwMzQ3OTY5MjExMDcyNTcy.XuEqUw.iP9vOTT_H2LeFptawKYTna-A9WU");
        builder.setActivity(Activity.watching("+help - ver.0.2.1"));
        builder.setStatus(OnlineStatus.ONLINE);

        CommandManager commandManager = registerCommands();
        builder.addEventListeners(
                new MessageReceiveListener(commandManager),
                new MessageReactionListener()
        );

        builder.build();
    }

    private static CommandManager registerCommands() {
        return registerCommands(new CommandManager());
    }

    private static CommandManager registerCommands(CommandManager commandManager) {

        commandManager.addCommand("ttt", new CreateGameCommand());
        commandManager.addCommand("lv", new LeaveGameCommand());
        commandManager.addCommand("help", new HelpCommand());

        return commandManager;
    }

}
