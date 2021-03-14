package BestBotEuWest.command.commands.nonAdmin;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.CommandManager;
import BestBotEuWest.command.*;
import BestBotEuWest.data.PrefixMap;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;

import java.util.List;

public class HelpCommand implements IPublicCommand, IHiddenCommand, IDmCommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handlePublic(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        String prefix = PrefixMap.PREFIXES.get(ctx.getGuild().getIdLong());

        // no search text given -> general help
        if (args == null || args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("Best Bot EUWest", "https://github.com/Philipp4Thiel/Best-Bot-EUWest");

            manager.getPublicCommands().stream().map(IPublicCommand::getName).forEach(
                    (it) -> embed.addField(""
                            , new ColoredStringAsciiDoc()
                                    .addBlueAboveEq(it.substring(0, 1).toUpperCase() + it.substring(1) + ":")
                                    .addOrange(prefix + "help " + it)
                                    .build()
                            , true)
            );
            channel.sendMessage(embed.build()).queue();
            return;
        }

        // specific help
        String search = args.get(0);
        IPublicCommand cmd = manager.searchPublicCommand(search);

        // command does not exist -> error message
        if (cmd == null) {
            commandNotFound(channel, search, prefix);
            return;
        }
        // command does exits -> command specific help
        MessageEmbed help = cmd.getPublicHelp(prefix);
        if (help != null) {
            channel.sendMessage(help).queue();
        }
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        String prefix = PrefixMap.PREFIXES.get(ctx.getGuild().getIdLong());

        // no search text given -> general help
        if (args == null || args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("Best Bot EUWest", "https://github.com/Philipp4Thiel/Best-Bot-EUWest");

            manager.getAdminCommands().stream().map(IAdminCommand::getName).forEach(
                    (it) -> embed.addField(""
                            , new ColoredStringAsciiDoc()
                                    .addBlueAboveEq(it.substring(0, 1).toUpperCase() + it.substring(1) + ":")
                                    .addOrange(prefix + "help " + it)
                                    .build()
                            , true)
            );
            channel.sendMessage(embed.build()).queue();
            return;
        }

        // specific help
        String search = args.get(0);
        IAdminCommand cmd = manager.searchAdminCommand(search);

        // command does not exist -> error message
        if (cmd == null) {
            commandNotFound(channel, search, prefix);
            return;
        }
        // command does exits -> command specific help
        channel.sendMessage(cmd.getAdminHelp(prefix)).queue();
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        String prefix = PrefixMap.PREFIXES.get(ctx.getGuild().getIdLong());

        // no search text given -> general help
        if (args == null || args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("Best Bot EUWest", "https://github.com/Philipp4Thiel/Best-Bot-EUWest");

            manager.getOwnerCommands().stream().map(IOwnerCommand::getName).forEach(
                    (it) -> embed.addField(""
                            , new ColoredStringAsciiDoc()
                                    .addBlueAboveEq(it.substring(0, 1).toUpperCase() + it.substring(1) + ":")
                                    .addOrange(prefix + "help " + it)
                                    .build()
                            , true)
            );
            channel.sendMessage(embed.build()).queue();
            return;
        }

        // specific help
        String search = args.get(0);
        IOwnerCommand cmd = manager.searchOwnerCommand(search);

        // command does not exist -> error message
        if (cmd == null) {
            commandNotFound(channel, search, prefix);
            return;
        }
        // command does exits -> command specific help
        channel.sendMessage(cmd.getOwnerHelp(prefix)).queue();
    }

    @Override
    public void handleHidden(CommandContext ctx) {
        switch (ctx.getPermissionLevel()) {
            case 0:
                handlePublic(ctx);
                break;
            case 1:
                handleAdmin(ctx);
                break;
            case 2:
                handleOwner(ctx);
                break;
        }
    }

    @Override
    public String getName() {
        return "help";
    }
/*
    @Override
    public String standardizedHelp() {
        //TODO
        return null;
    }*/

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("A command that shows you what my bot can or can't do.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "help")
                .build(), false);

        // specific command
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("specific command:")
                .addNormal("with this subcommand you can see what another command does.")
                .addOrange(prefix + "help <command>")
                .build(), false);

        return embed.build();
    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }

    void commandNotFound(TextChannel channel, String search, String prefix) {
        channel.sendMessage(
                EmbedUtils.getDefaultEmbed()
                        .setTitle("Error: Command `" + search + "` not found")
                        .setDescription(new ColoredStringAsciiDoc()
                                .addBlueAboveDash("try " + prefix + "help to see all commands")
                                .build())
                        .build()
        ).queue();
    }

    @Override
    public void handleDM(CommandContext ctx) {
        JSONObject json = new JSONObject();

        json.put("owner", "Lauch4P#2261");
        json.put("prefix", "please");
    }
}