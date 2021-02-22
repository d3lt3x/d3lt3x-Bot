package me.d3lt3x.discord.bot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

public class MessageUtil {


    public static MessageEmbed messageEmbed(String title, int color, String[] field, String[] value, boolean inline) {

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setColor(color);

        for (int i = 0; i < field.length; i++)
            embedBuilder.addField(field[i], value[i], inline);

        return embedBuilder.build();
    }


    public static MessageEmbed messageEmbed(String title, int color, String field, String value, boolean inline) {

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setColor(color);

        embedBuilder.addField(field, value, inline);

        return embedBuilder.build();
    }

    public static String removeIllegalMentions(Message input) {

        String output = input.getContentRaw();

        for (Role role : input.getMentionedRoles()) {
            output = output.replace(role.getAsMention(), role.getName());
        }

        output = output.replace("@everyone", "everyone").replace("@here", "here");

        return output;
    }

}
