package com.pthiel.JavaLauch.command.commands.admin;

import com.pthiel.JavaLauch.Config;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;

public class OwnerPingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        String author = ctx.getAuthor().getAsMention();

        ctx.getChannel().sendMessage(
                EmbedUtils.getDefaultEmbed()
                        .setDescription("Shame on you " + ctx.getAuthor().getAsMention() + " for pinging my stupid boss")
                        .setImage(Config.get("png_path") + "getoutofmyroomimplaingWOW.png")
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
