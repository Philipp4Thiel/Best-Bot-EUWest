package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Message;

public class VanishCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage(
                EmbedUtils
                        .getDefaultEmbed()
                        .setTitle("This message will disappear in a few seconds")
                        .build()
        ).queue();

        // too stupid to implement delete
    }

    @Override
    public String getName() {
        return "vanish";
    }

    @Override
    public String getHelp() {
        return "answer disappears after a few seconds";
    }

    @Override
    public String getUsage() {
        return "vanish";
    }
}
