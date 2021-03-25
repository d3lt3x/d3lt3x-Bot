package me.d3lt3x.discord.bot.command.general;

import me.d3lt3x.discord.bot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class BullyCommand extends Command {

    public final static Map<User, String> BULLY_LIST = new HashMap<>();

    public BullyCommand(String commandLabel, String descriptionLabel, String descriptionValue) {
        super(commandLabel, descriptionLabel, descriptionValue);
    }


    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString) {

        if (args.length < 2 || message.getMentionedUsers().size() != 1 || !message.getMentionedUsers().toString().contains(args[0])) {
            getCommandManager().sendSyntax(channel, "bully");
            return;
        }

        if (!channel.getType().isGuild()) {
            channel.sendMessage("This command doesn't work in private channels.").queue();
            return;
        }


        User bulliedUser = message.getMentionedMembers().get(0).getUser();


        BULLY_LIST.remove(bulliedUser);
        BULLY_LIST.put(bulliedUser, argsAsString.substring(argsAsString.indexOf(bulliedUser.getAsMention()) + args[0].length()));

        channel.sendMessage("I will bully " + bulliedUser.getAsMention() + " now.").queue();

    }
}
