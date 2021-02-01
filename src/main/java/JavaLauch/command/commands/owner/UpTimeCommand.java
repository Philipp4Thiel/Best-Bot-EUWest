package JavaLauch.command.commands.owner;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Duration;
import java.time.Instant;

public class UpTimeCommand implements IOwnerCommand {

    private final Instant start;


    public UpTimeCommand() {
        start = Instant.now();
    }

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        Instant now = Instant.now();
        Duration timeElapsed = Duration.between(start, now);

        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                .setTitle("Uptime")
                .setDescription(timeElapsed.toMinutes() +" minutes")
                .build()).queue();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("This command shows the time since last reboot.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "uptime")
                .build(), false);

        return embed.build();
    }
}
