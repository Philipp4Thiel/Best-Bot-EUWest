package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ReloadCommand implements IOwnerCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadCommand.class);

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void handleOwner(CommandContext ctx) {

        try {
            LOGGER.info("trying to restart BOT");
            Runtime.getRuntime().exec("");
            System.exit(1);
        } catch (IOException e) {
            LOGGER.info("failed to restart BOT");
            ctx.getChannel().sendMessage("couldn't reload").queue();
        }
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("This command is used to reload the bot.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "reload")
                .build(), false);

        return embed.build();
    }
}
