package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IPublicCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ReactCommand implements IPublicCommand {

    private final JDA bot;

    public ReactCommand(JDA bot) {
        this.bot = bot;
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        handlePublic(ctx);
    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public String getName() {
        return "react";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        handlePublic(ctx);
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public void handlePublic(CommandContext ctx) {
        ctx.getChannel().retrieveMessageById(ctx.getArgs().get(1)).complete().addReaction(bot.getEmotesByName(ctx.getArgs().get(0), true).get(0)).complete();
        ctx.getMessage().delete().queue();
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("A command that shows you what my bot can or can't do.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "react <emote name> <msg id>")
                .build(), false);

        return embed.build();
    }
}
