package me.d3lt3x.discord.bot.game;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
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

    private final List<String> board = new ArrayList<>();
    private boolean multiPlayer;
    private User userInRow = null;
    private int rowCount = 0;
    private boolean pause = false;
    private Message beginner;

    public TicTacToeGame(User user, MessageChannel channel) {

        if (isInGameCheckAndSendMessage(user, channel)) return;

        this.player1 = user;
        setupGame(channel);
    }

    public TicTacToeGame(User user1, User user2, MessageChannel channel) {

        if (checkIfUserInAnyGame(user1, channel) || checkIfUserInAnyGame(user2, channel)) return;
       
        this.player1 = user1; 

        if (!user2.isBot() && !user1.equals(user2)) {
            multiPlayer = true;
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

        message = message.editMessage(message.getContentRaw() + "\n" + user.getAsMention() + " left the game.").complete();
        stop();

    }

    public void declareWinner(String user) {
        stop();
        this.message = this.message.editMessage(this.message.getContentRaw() + "\n " + user + " won!").complete();

    }

    private void setupGame(MessageChannel channel) {

        int random = ThreadLocalRandom.current().nextInt(0, 2);

        if (!multiPlayer && random == 1)
            this.beginner = channel.sendMessage("The bot begins!").complete();

        else {
            userInRow = getPlayers().get(random);
            this.beginner = channel.sendMessage(userInRow.getAsMention() + " begins!").complete();
        }

        this.message = channel.sendMessage("⬛⬛⬛\n⬛⬛⬛\n⬛⬛⬛").complete();

        TIC_TAC_TOE_GAMES.put(message, this);

        for (String reaction : REACTIONS)
            message.addReaction(reaction).queue();

        if (userInRow == null) botSelect(channel.getJDA());
    }


    public void pick(User user, MessageReaction reaction) {

        reaction.removeReaction(user).queue();

        if (!userInRow.equals(user)) return;
        if (pause) return;


        reaction.clearReactions().queue();

        String reactionStr = reaction.getReactionEmote().getEmoji();
        if (!REACTIONS.contains(reactionStr)) return;

        int pos = REACTIONS.indexOf(reactionStr);

        if (board.get(pos) != null) return;

        board.set(pos, user.getAsMention());

        set(reaction.getJDA(), pos, USER_EMOTES[getPlayers().indexOf(user)]);

    }

    private void set(JDA jda, int pos, String emote) {

        if (rowCount == 1) {
            this.beginner.delete().queue();
            this.beginner = null;
        }

        rowCount++;

        if (pos >= 3) {
            if (pos <= 5)
                pos++;
            else pos += 2;
        }

        StringBuilder builder = new StringBuilder(this.message.getContentRaw()).replace(pos, pos + 1, emote);
        this.message = this.message.editMessage(builder).complete();

        getWinner();

        if (multiPlayer) {

            if (userInRow.equals(player1))
                userInRow = player2;
            else userInRow = player1;

        } else if (userInRow == null) {
            userInRow = player1;
        } else botSelect(jda);

    }

    private void botSelect(JDA jda) {

        if (pause) return;
        this.userInRow = null;

        int random = ThreadLocalRandom.current().nextInt(9);

        while (board[random] != null) {
            random = ThreadLocalRandom.current().nextInt(9);
        }

        this.message.clearReactions(REACTIONS.get(random)).queue();

        Arrays.fill(board, random, random + 1, jda.getSelfUser().getAsMention());

        set(jda, random, "⭕");

        this.userInRow = player1;

    }

    private void getWinner() {

        if (rowCount < 3) return;

        String comparator;

        for (int[] section : POSSIBILITIES) {

            if (board[section[0]] == null || board[section[1]] == null || board[section[2]] == null) continue;

            comparator = board[section[0]];

            if (comparator.equals(board[section[1]]) && comparator.equals(board[section[2]])) {

                declareWinner(comparator);
                return;
            }
        }

        if (rowCount >= board.length) declareWinner("Nobody");

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
        pause = true;
        this.message.clearReactions().queue();
        TIC_TAC_TOE_GAMES.remove(this.message);
        if (this.beginner != null) this.beginner.delete().queue();
    }

    public List<User> getPlayers() {
        return Arrays.asList(player1, player2);
    }

    public boolean isMultiPlayer() {
        return multiPlayer;
    }

    private boolean isInGame(User user) {
        return user == player1 || user == player2;
    }

}


