package me.d3lt3x.discord.bot.command;

public abstract class BotCommand implements Command{

    private boolean enabled = true;

    @Override
    public void enable(boolean enable) {
        enabled = enable;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
