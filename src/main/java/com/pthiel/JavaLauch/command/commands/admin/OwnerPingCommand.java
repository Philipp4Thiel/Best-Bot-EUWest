package com.pthiel.JavaLauch.command.commands.admin;

import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;

public class OwnerPingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        String author = ctx.getAuthor().getAsMention();

        ctx.getChannel().sendMessage(
                EmbedUtils.getDefaultEmbed()
                        .setDescription(author + " stop pinging my owner")
                        .setImage("https://raw.githubusercontent.com/Philipp4Thiel/Best-Bot-EUWest/master/randomStuff/getoutofmyroomimplaingWOW.png")
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "ownerping";
    }

    @Override
    public String getHelp() {
        return "Not a useful command just something that happens when my stupid boss gets pinged";
    }

    @Override
    public String getUsage() {
        return "this command isn't meant to be used";
    }
}
