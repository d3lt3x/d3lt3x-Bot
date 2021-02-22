package me.d3lt3x.discord.bot.command.general;

import me.d3lt3x.discord.bot.command.Command;
import me.d3lt3x.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class UnBullyCommand implements Command {

    @Override
    public void onCommand(User user, MessageChannel channel, Message message, String[] args, String argsAsString) {

        if (args.length > 1 || message.getMentionedUsers().size() != 1 || message.getMentionedUsers().toString().contains(args[0])) {
            channel.sendMessage(MessageUtil.messageEmbed("Syntax Error", 0xFF0042, "**Use:**", "`+unbully @User` to stop bullying a user.", false)).queue();
            return;
        }

        if (!channel.getType().isGuild()) {
            channel.sendMessage("This command doesn't work in private channels.").queue();
            return;
        }

        User bulliedUser = message.getMentionedMembers().get(0).getUser();

        if (BullyCommand.BULLY_LIST.containsKey(bulliedUser)) {
            BullyCommand.BULLY_LIST.remove(bulliedUser);
            channel.sendMessage("Ok, i will stop bullying " + bulliedUser.getAsMention() + " now :(").queue();
        } else channel.sendMessage(bulliedUser.getAsMention() + " is not being bullied right now.").queue();

    }
}
