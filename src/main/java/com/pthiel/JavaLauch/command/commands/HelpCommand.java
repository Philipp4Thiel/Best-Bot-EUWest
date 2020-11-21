package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.CommandManager;
import com.pthiel.JavaLauch.Config;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
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
            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("List of commands");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.addField(it.substring(0, 1).toUpperCase() + it.substring(1) + ":"
                            , "`" + Config.get("prefix") + "help " + it + "`"
                            , true)
                            .setFooter("requested by: " + ctx.getAuthor().getName())
            );

            channel.sendMessage(builder.build()).queue();

            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage(new EmbedBuilder()
                    .setTitle("Error: Command `" + search + "` not found")
                    .setDescription("try `-help` to see all commands")
                    .setFooter("requested by: " + ctx.getAuthor().getName())
                    .build()).queue();
            return;
        }

        channel.sendMessage(command.getHelp()
                .setFooter("requested by: " + ctx.getAuthor().getName())
                .build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public EmbedBuilder getHelp() {
        return new EmbedBuilder()
                .setTitle("Help")
                .setDescription("Shows the list with commands in the bot")
                .addField("Usage:"
                        , "`" + Config.get("prefix") + "help [command]`"
                        , true);
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }
}
