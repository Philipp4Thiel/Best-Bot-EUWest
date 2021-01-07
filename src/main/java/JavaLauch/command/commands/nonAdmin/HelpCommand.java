package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.CommandManager;
import JavaLauch.command.CommandContext;
import JavaLauch.command.ICommand;
import JavaLauch.data.PrefixMap;
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
        String prefix = PrefixMap.PREFIXES.get(ctx.getGuild().getIdLong());

        if (args == null || args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("Best Bot EUWest", "https://github.com/Philipp4Thiel/Best-Bot-EUWest");

            manager.getCommands().stream().map(ICommand::getName).forEach(
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

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage(
                    EmbedUtils.getDefaultEmbed()
                            .setTitle("Error: Command `" + search + "` not found")
                            .setDescription(new ColoredStringAsciiDoc()
                                    .addBlueAboveDash("try " + prefix + "help to see all commands")
                                    .build())
                            .build()
            ).queue();
            return;
        }

        channel.sendMessage(
                EmbedUtils
                        .getDefaultEmbed()
                        .setTitle(search.substring(0, 1).toUpperCase() + search.substring(1) + ":")
                        .setDescription(new ColoredStringAsciiDoc()
                                .addBlueAboveDash(command.getHelp())
                                .build())
                        .addField(
                                "Usage:"
                                , new ColoredStringAsciiDoc()
                                        .addOrange(prefix + command.getUsage())
                                        .build()
                                , true
                        ).build()
        ).queue();
    }

    @Override
    public void handle(CommandContext ctx, boolean notAsCmd) {
        handle(ctx);
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
        return List.of("commands", "cmds", "commandlist","selfping");
    }
}
