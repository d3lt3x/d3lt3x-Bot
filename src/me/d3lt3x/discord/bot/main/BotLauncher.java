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

    private static CommandManager commandManager;

    public static void main(String[] args) throws LoginException {

        JDABuilder builder = JDABuilder.createDefault("");
        builder.setActivity(Activity.watching("+help - ver.0.3.5"));
        builder.setStatus(OnlineStatus.ONLINE);

        commandManager = registerCommands();
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

        commandManager.addCommand(new CreateGameCommand("ttt", "Use `+ttt` or `+ttt @User`", "to start a TicTacToe game."))
                .addCommand(new LeaveGameCommand("lv", "Use `+lv`", "to leave a game."))
                .addCommand(new ResponseCommand("rp", "Use `+rp add/remove`", "to add or remove a response."))
                .addCommand(new HelpCommand("help", "Use `+help`", "for help."))
                .addCommand(new BullyCommand("bully", "Use `+bully @User <message>`", "to bully someone."))
                .addCommand(new UnBullyCommand("unbully", "Use `+unbully @User`", "to stop bullying."));


        return commandManager;
    }

}
