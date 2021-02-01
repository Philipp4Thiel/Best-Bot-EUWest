package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.ColoredStrings.ColoredStringDiff;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IPublicCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PingCommand implements IPublicCommand {

    @Override
    public void handlePublic(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel().sendMessage(
                        EmbedUtils.getDefaultEmbed()
                                .setTitle(":parking::o2::ng:")
                                .addField("Ping:"
                                        , new ColoredStringDiff()
                                                .addRed(ping + "ms", true)
                                                .build()
                                        , true)
                                .addField("WS ping:"
                                        , new ColoredStringDiff()
                                        .addRed(jda.getGatewayPing() + "ms",true)
                                        .build()
                                        , true)
                                .build()
                ).queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        this.handlePublic(ctx);
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("A really simple ping command.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use")
                .addOrange(prefix + "ping")
                .build(), false);

        return embed.build();    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        this.handlePublic(ctx);
    }
}
