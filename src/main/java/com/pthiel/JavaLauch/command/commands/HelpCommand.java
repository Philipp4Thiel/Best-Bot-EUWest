package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.CommandManager;
import com.pthiel.JavaLauch.Config;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            builder.append("List of commands\n");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append("'").append(Config.get("prefix")).append(it).append("'\n")
            );

            channel.sendMessage(builder.toString()).queue();

            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Nothing found for " + search).queue();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list with commands in the bot\n" +
                "usage: '" + Config.get("prefix") + "help [command]'";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }
}
