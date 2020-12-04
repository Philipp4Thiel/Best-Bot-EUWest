package com.pthiel.JavaLauch.command;

import java.util.List;

public interface ICommand {

    void handle(CommandContext ctx);
    void handle(CommandContext ctx, boolean notAsCmd);

    String getName();

    String getHelp();

    String getUsage();

    default List<String> getAliases() {
        return List.of();
    }
}
