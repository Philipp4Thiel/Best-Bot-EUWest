package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;

public class TestCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage(
                EmbedUtils
                        .getDefaultEmbed()
                        .setTitle("Test")
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return "just a command to test stuff";
    }

    @Override
    public String getUsage() {
        return "test";
    }
}
