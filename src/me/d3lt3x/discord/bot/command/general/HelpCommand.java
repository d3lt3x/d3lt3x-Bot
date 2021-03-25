package me.d3lt3x.discord.bot.command.general;

import me.d3lt3x.discord.bot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class HelpCommand extends Command {


    public HelpCommand(String commandLabel, String descriptionLabel, String descriptionValue) {
        super(commandLabel, descriptionLabel, descriptionValue);
    }

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString) {

        channel.sendMessage(getCommandManager().getCommandSyntaxList()).queue();

    }
}
