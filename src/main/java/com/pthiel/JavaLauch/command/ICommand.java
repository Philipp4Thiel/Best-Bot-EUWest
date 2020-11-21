package com.pthiel.JavaLauch.command;

import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    String getUsage();

    default List<String> getAliases() {
        return List.of();
    }
}
