package JavaLauch.command.commands.owner;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrintInIDECommand implements IOwnerCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintInIDECommand.class);

    @Override
    public void handleOwner(CommandContext ctx) {

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
    public String getName() {
        return "log";
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("A simple command to send stuff to the log of the bot");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "log <msg>")
                .build(), false);

        return embed.build();
    }

    @Override
    public List<String> getAliases() {
        return List.of("print", "logger");
    }
}
