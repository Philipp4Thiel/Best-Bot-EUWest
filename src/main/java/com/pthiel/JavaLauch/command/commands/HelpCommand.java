package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.CommandManager;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import com.pthiel.JavaLauch.data.PrefixMap;
import me.duncte123.botcommons.messaging.EmbedUtils;
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
        String rawPrefix = PrefixMap.PREFIXES.get(ctx.getGuild().getIdLong());

        String prefix = rawPrefix.equals("<@!776555901724917800>") ? "" : rawPrefix;

        if (args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("Best Bot EUWest","https://github.com/Philipp4Thiel/Best-Bot-EUWest");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> embed.addField(it.substring(0, 1).toUpperCase() + it.substring(1) + ":"
                            , "```" + prefix + "help " + it + "```"
                            , true)
            );

            channel.sendMessage(embed.build()).queue();

            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage(
                    EmbedUtils.getDefaultEmbed()
                            .setTitle("Error: Command `" + search + "` not found")
                            .setDescription("```try " + prefix + "help to see all commands```")
                            .build()
            ).queue();
            return;
        }

        channel.sendMessage(
                EmbedUtils
                        .getDefaultEmbed()
                        .setTitle(search.substring(0, 1).toUpperCase() + search.substring(1))
                        .setDescription("```"+command.getHelp()+"```")
                        .addField(
                                "Usage",
                                "```" + prefix + command.getUsage() + "```",
                                true
                        ).build()
        ).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list with commands in the bot";
    }

    @Override
    public String getUsage() {
        return "help [command]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }
}
