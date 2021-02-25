package JavaLauch.command.commands.owner;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutDownCommand implements IOwnerCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownCommand.class);

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        ctx.getChannel().sendMessage("force shutdown...").queue();
        LOGGER.info("force shutdown by "+ctx.getAuthor().getAsTag());
        System.exit(0);
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("This command is used to force shutdown the bot.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "shutdown")
                .build(), false);

        return embed.build();
    }
}
