package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import com.pthiel.JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrintInIDECommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintInIDECommand.class);

    @Override
    public void handle(CommandContext ctx) {

        final List<String> args = ctx.getArgs();
        final String context = String.join(" ", args);
        ;
        LOGGER.info(context);

        ctx.getChannel().sendMessage(
                EmbedUtils.getDefaultEmbed()
                        .setTitle("Sent to log")
                        .setDescription(
                                new ColoredStringAsciiDoc()
                                        .addOrange(" " + context + " ")
                                        .build()
                        )
                        .build()
        ).queue();
    }

    @Override
    public void handle(CommandContext ctx, boolean notAsCmd) {
        return;
    }

    @Override
    public String getName() {
        return "log";
    }

    @Override
    public String getHelp() {
        return "A command to print stuff in the log";
    }

    @Override
    public String getUsage() {
        return "log [text]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("print", "logger");
    }
}
