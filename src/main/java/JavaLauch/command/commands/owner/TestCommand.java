package JavaLauch.command.commands.owner;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.ColoredStrings.ColoredStringDiff;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class TestCommand implements IOwnerCommand {

    @Override
    public void handleOwner(CommandContext ctx) {
        ctx.getChannel().sendMessage(
                EmbedUtils
                        .getDefaultEmbed()
                        .setTitle("Testing out the Embed/Code coloring features")
                        .addField("AsciiDoc Coloring"
                                , new ColoredStringAsciiDoc().addBlue("blue text")
                                        .addBlueAboveDash("blue text above dashes")
                                        .addBlueAboveDash("blue text above 4 dashes", 4)
                                        .addBlueAboveEq("blue text above equals")
                                        .addBlueAboveEq("blue text above 3 equals", 3)
                                        .addNormal("normal text")
                                        .addOrange("orange text")
                                        .build()
                                , true)
                        .addField("Diff Coloring"
                                , new ColoredStringDiff()
                                        .addGreen("unsymmetrical green")
                                        .addGreen("specified unsymmetrical green", false)
                                        .addGreen("symmetrical green", true)
                                        .addRed("unsymmetrical red")
                                        .addRed("specified unsymmetrical red", false)
                                        .addRed("symmetrical red", true)
                                        .addNormal("normal white text")
                                        .addGrayDashes("unsymmetrical gray dashes")
                                        .addGrayDashes("specified unsymmetrical gray dashes", false)
                                        .addGrayDashes("symmetrical gray dashes", true)
                                        .addGrayTimes("unsymmetrical gray times")
                                        .addGrayTimes("specified unsymmetrical gray times", false)
                                        .addGrayTimes("symmetrical gray times", true)
                                        .build()
                                , true)
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("This is just a command made for testing purposes.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "test")
                .build(), false);

        return embed.build();
    }
}
