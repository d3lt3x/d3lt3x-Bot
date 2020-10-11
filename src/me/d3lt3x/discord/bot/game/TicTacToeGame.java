package me.d3lt3x.discord.bot.game;

import me.d3lt3x.discord.bot.main.BotLauncher;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TicTacToeGame {

    private Message message;

    private final static Map<Message, TicTacToeGame> TIC_TAC_TOE_GAMES = new HashMap<>();

    private final List<User> players = new ArrayList<>();
    private final String[] reactions = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};
    private final String[] game = new String[9];
    private final int[][] possibilities = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private boolean multiPlayer;
    private User userInRow = null;
    private int rowCount = 0;
    private boolean pause = false;
    private final String[] userEmotes = {"❌", "⭕"};
    private Message beginner;

    public TicTacToeGame(User user, MessageChannel channel) {

        if (isInGameCheck(user, channel)) return;
        players.add(user);
        setupGame(channel);
    }

    public TicTacToeGame(User user1, User user2, MessageChannel channel) {

        if (isInGameCheck(user1, channel)) return;
        if (isInGameCheck(user2, channel)) return;
        if (user2.isBot() || user1.equals(user2)) {
            players.add(user1);
            setupGame(channel);
            return;
        }

        players.add(user1);
        players.add(user2);
        multiPlayer = true;
        setupGame(channel);
    }

    private boolean isInGameCheck(User user, MessageChannel channel) {

        TicTacToeGame game = getGame(user);

        if (game == null) return false;

        if (game.multiPlayer) {
            for (User current : game.getPlayers()) {
                if (current.equals(user)) continue;
                channel.sendMessage(user.getAsMention() + " is already in a game with " + current.getAsMention() + ", you can leave with ``+lv``").queue();
            }
        } else {
            channel.sendMessage("You are already in a game, you can leave with ``+lv``").queue();
        }
        return true;
    }


    public static TicTacToeGame terminateAbandonedGames(User user1) {
        for (TicTacToeGame game : TIC_TAC_TOE_GAMES.values()) {
            if (game.players.contains(user1)) {
                game.message = game.message.editMessage(game.message.getContentRaw() + "\n" + user1.getAsMention() + " left the game.").complete();
                game.stop();
                return game;
            }
        }
        return null;
    }

    public void win(String user) {
        stop();
        this.message = this.message.editMessage(this.message.getContentRaw() + "\n " + user + " won!").complete();

    }

    private void setupGame(MessageChannel channel) {

        int random = ThreadLocalRandom.current().nextInt(0, 2);

        if (!multiPlayer && random == 1)
            this.beginner = channel.sendMessage("The bot begins!").complete();

        else {
            userInRow = players.get(random);
            this.beginner = channel.sendMessage(userInRow.getAsMention() + " begins!").complete();
        }

        this.message = channel.sendMessage("⬛⬛⬛\n⬛⬛⬛\n⬛⬛⬛").complete();

        TIC_TAC_TOE_GAMES.put(message, this);

        for (String reaction : reactions)
            message.addReaction(reaction).queue();

        if (userInRow == null) botSelect();
    }


    public static boolean isInGame(Message message, User user) {
        return TIC_TAC_TOE_GAMES.get(message).players.contains(user);
    }

    public static boolean isInGame(User user) {
        for (TicTacToeGame game : TIC_TAC_TOE_GAMES.values()) {
            if (game.players.contains(user)) return true;
        }
        return false;
    }

    public static boolean isGame(Message message) {
        return TIC_TAC_TOE_GAMES.containsKey(message);
    }

    public void pick(User user, MessageReaction reaction) {

        reaction.removeReaction(user).queue();

        if (!userInRow.equals(user)) return;
        if (pause) return;


        reaction.clearReactions().queue();

        if (!Arrays.asList(reactions).contains(reaction.getReactionEmote().getEmoji())) return;

        String reactionStr = reaction.getReactionEmote().getEmoji();
        int pos = Arrays.asList(reactions).indexOf(reactionStr);

        if (Arrays.asList(game).get(pos) != null) return;

        Arrays.fill(game, pos, pos + 1, user.getAsMention());

        set(pos, this.userEmotes[players.indexOf(user)]);

    }

    private void set(int pos, String emote) {

        if (rowCount == 1) {
            this.beginner.delete().queue();
            this.beginner = null;
        }

        rowCount++;

        if (pos >= 3) {
            if (pos <= 5) pos++;
            else pos += 2;
        }

        StringBuilder builder = new StringBuilder(this.message.getContentRaw());
        builder.replace(pos, pos + 1, emote);
        this.message = this.message.editMessage(builder.toString()).complete();

        getWinner();

        if (multiPlayer) {

            if (userInRow.equals(players.get(0))) userInRow = players.get(1);
            else userInRow = players.get(0);

        } else if (userInRow == null) {
            userInRow = players.get(0);
        } else botSelect();

    }

    private void botSelect() {

        if (pause) return;
        this.userInRow = null;

        int random = ThreadLocalRandom.current().nextInt(0, 8 + 1);

        while (Arrays.asList(game).get(random) != null) {
            random = ThreadLocalRandom.current().nextInt(0, 8 + 1);
        }

        this.message.clearReactions(reactions[random]).queue();

        Arrays.fill(game, random, random + 1, BotLauncher.getJda().getSelfUser().getAsMention());

        set(random, "⭕");

        this.userInRow = players.get(0);

    }

    private void getWinner() {

        if (rowCount < 3) return;

        String comparator;

        for (int[] section : possibilities) {

            if (game[section[0]] == null || game[section[1]] == null || game[section[2]] == null) continue;

            comparator = game[section[0]];

            if (comparator.equals(game[section[1]]) && comparator.equals(game[section[2]])) {

                win(comparator);
                return;
            }
        }

        if (rowCount > 8) {
            win("Nobody");
        }

    }

    public static TicTacToeGame getGame(Message message) {
        return TIC_TAC_TOE_GAMES.get(message);
    }

    public static TicTacToeGame getGame(User user) {
        for (TicTacToeGame game : TIC_TAC_TOE_GAMES.values()) {
            if (game.players.contains(user)) {
                return game;
            }
        }
        return null;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void stop() {
        pause = true;
        this.message.clearReactions().queue();
        TIC_TAC_TOE_GAMES.remove(this.message);
        if (this.beginner != null) this.beginner.delete().queue();
    }

}


