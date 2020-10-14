package me.d3lt3x.discord.bot.game;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TicTacToeGame {

    private final static Map<Message, TicTacToeGame> TIC_TAC_TOE_GAMES = new HashMap<>();
    private final static List<String> REACTIONS = Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣");
    private final static int[][] POSSIBILITIES = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private final static String[] USER_EMOTES = {"❌", "⭕"};

    private Message message;
    private User player1;
    @Nullable
    private User player2;

    private final String[] board = new String[9];
    private boolean multiPlayer;
    private User userInRow = null;
    private int rowCount = 0;
    private boolean pause = false;
    private Message beginner;


    public TicTacToeGame(User user, MessageChannel channel) {

        if (checkIfUserInAnyGame(user, channel)) return;
        this.player1 = user;

        setupGame(channel);
    }


    public TicTacToeGame(User user1, @Nullable User user2, MessageChannel channel) {

        if (checkIfUserInAnyGame(user1, channel) || checkIfUserInAnyGame(user2, channel)) return;
        this.player1 = user1;

        if (user2 != null && !user2.isBot() && !user1.equals(user2)) {
            this.multiPlayer = true;
            this.player2 = user2;
        }

        setupGame(channel);
    }


    private boolean checkIfUserInAnyGame(User user, MessageChannel channel) {

        TicTacToeGame game = getGame(user);
        if (game == null) return false;

        if (game.multiPlayer) {
            for (User current : getPlayers()) {
                if (current.equals(user)) continue;
                channel.sendMessage(user.getAsMention() + " is already in a game with " + current.getAsMention() + ", you can leave with ``+lv``").queue();
            }
        } else {

            channel.sendMessage("You are already in a game, you can leave with ``+lv``").queue();
        }

        return true;
    }


    public void leaveGame(User user) {

        if (!isInGame(user)) return;

        stop();
        this.message.editMessage(this.message.getContentRaw() + "\n" + user.getAsMention() + " left the game.").queue(message -> this.message = message);
    }


    public void declareWinner(String user) {

        stop();
        this.message.editMessage(this.message.getContentRaw() + "\n " + user + " won!").queue(message -> this.message = message);
    }


    private void setupGame(MessageChannel channel) {

        int random = ThreadLocalRandom.current().nextInt(2);

        if (!this.multiPlayer && random == 1)
            this.beginner = channel.sendMessage("The bot begins!").complete();

        else {
            this.userInRow = getPlayers().get(random);
            this.beginner = channel.sendMessage(this.userInRow.getAsMention() + " begins!").complete();
        }

        this.message = channel.sendMessage("⬛⬛⬛\n⬛⬛⬛\n⬛⬛⬛").complete();

        TIC_TAC_TOE_GAMES.put(this.message, this);

        for (String reaction : REACTIONS)
            this.message.addReaction(reaction).queue();

        if (this.userInRow == null) botSelect(channel.getJDA());


    }


    public void pick(User user, MessageReaction reaction) {

        reaction.removeReaction(user).queue();

        if (this.userInRow == null || !this.userInRow.equals(user)) return;
        if (this.pause) return;


        reaction.clearReactions().queue();

        String reactionStr = reaction.getReactionEmote().getEmoji();
        if (!REACTIONS.contains(reactionStr)) return;

        int pos = REACTIONS.indexOf(reactionStr);

        if (this.board[pos] != null) return;

        Arrays.fill(this.board, pos, pos + 1, user.getAsMention());

        set(reaction.getJDA(), pos, USER_EMOTES[getPlayers().indexOf(user)]);

    }


    private void set(JDA jda, int pos, String emote) {

        if (this.rowCount == 0) {
            this.beginner.delete().queue();
            this.beginner = null;
        }

        this.rowCount++;

        if (pos >= 3) {
            if (pos <= 5)
                pos++;
            else pos += 2;
        }

        StringBuilder builder = new StringBuilder(this.message.getContentRaw()).replace(pos, pos + 1, emote);

        this.message.editMessage(builder).queue(message -> {

            this.message = message;

            getWinner();

            if (this.multiPlayer) {

                if (this.userInRow.equals(this.player1))
                    this.userInRow = this.player2;

                else this.userInRow = this.player1;

            } else if (this.userInRow.equals(player1)) {
                botSelect(jda);

            } else this.userInRow = this.player1;

        });
    }


    private void botSelect(JDA jda) {

        if (this.pause) return;
        this.userInRow = null;

        int random = ThreadLocalRandom.current().nextInt(9);

        while (this.board[random] != null) {
            random = ThreadLocalRandom.current().nextInt(9);
        }

        this.message.clearReactions(REACTIONS.get(random)).queue();
        Arrays.fill(this.board, random, random + 1, jda.getSelfUser().getAsMention());
        set(jda, random, "⭕");

    }


    private void getWinner() {

        if (this.rowCount < 3) return;

        String comparator;

        for (int[] section : POSSIBILITIES) {

            if (this.board[section[0]] == null || this.board[section[1]] == null || this.board[section[2]] == null) continue;

            comparator = this.board[section[0]];

            if (comparator.equals(this.board[section[1]]) && comparator.equals(this.board[section[2]])) {

                declareWinner(comparator);
                return;
            }
        }

        if (this.rowCount >= this.board.length) declareWinner("Nobody");

    }


    public static TicTacToeGame getGame(Message message) {
        return TIC_TAC_TOE_GAMES.get(message);
    }


    public static TicTacToeGame getGame(User user) {

        for (TicTacToeGame game : TIC_TAC_TOE_GAMES.values())
            if (game.isInGame(user))
                return game;

        return null;
    }


    public void stop() {
        this.pause = true;
        if (this.message != null) this.message.clearReactions().queue();
        TIC_TAC_TOE_GAMES.remove(this.message);
        if (this.beginner != null) this.beginner.delete().queue();
    }


    public List<User> getPlayers() {
        return Arrays.asList(this.player1, this.player2);
    }


    public boolean isMultiPlayer() {
        return this.multiPlayer;
    }


    private boolean isInGame(User user) {
        return user == this.player1 || user == this.player2;
    }

}


