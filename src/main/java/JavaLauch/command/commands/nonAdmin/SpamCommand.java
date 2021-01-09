package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IPublicCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SpamCommand implements IPublicCommand {

    final int MAX_REPS = 10;

    public void handlePublic(CommandContext ctx) {

        List<String> args = ctx.getArgs();

        if (args == null || args.isEmpty()) {
            sendError(ctx.getChannel(), "Missing args", "Use the help feature to get the usage of this command");
            return;
        }

        String first = args.get(0);

        int times;
        try {
            times = Integer.parseInt(first);
        } catch (NumberFormatException e) {
            sendError(ctx.getChannel(), "\"" + first + "\" is not a valid integer", "use the help feature to get the usage of this command or report a bug if this is an error");
            return;
        }

        List<String> withoutFirst = args.subList(1, args.size());
        if (withoutFirst.isEmpty()) {
            sendError(ctx.getChannel(), "no text to repeat", "please use the help feature to get the usage of this command\nor use the bugreport feature if this is a false error");
            return;
        }

        if (times > MAX_REPS) {
            times = MAX_REPS;
            ctx.getChannel().sendMessage("thanks to @xharlock#0001 this command is limited to " + MAX_REPS + " repetitions for non power people").queue();
        }

        String msg = String.join(" ", withoutFirst);

        spamMsg(msg, ctx.getChannel(), times);
    }

    @Override
    public String getName() {
        return "spam";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        this.handleAdmin(ctx);
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("A command to spam a desired message in this channel up to " + MAX_REPS + " times.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "spam <number of repetitions> <msg>")
                .build(), false);

        return embed.build();
    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("A command to spam a desired message in this channel. (Attention for admins the amount is uncapped so use it wisely)");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "spam <number of repetitions> <msg>")
                .build(), false);

        return embed.build();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return getAdminHelp(prefix);
    }

    @Override
    public List<String> getAliases() {
        return List.of("repeat");
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        List<String> args = ctx.getArgs();

        if (args == null || args.isEmpty()) {
            sendError(ctx.getChannel(), "Missing args", "Use the help feature to get the usage of this command");
            return;
        }

        String first = args.get(0);

        int times;
        try {
            times = Integer.parseInt(first);
        } catch (NumberFormatException e) {
            sendError(ctx.getChannel(), "\"" + first + "\" is not a valid integer", "use the help feature to get the usage of this command or report a bug if this is an error");
            return;
        }

        List<String> withoutFirst = args.subList(1, args.size());
        if (withoutFirst.isEmpty()) {
            sendError(ctx.getChannel(), "no text to repeat", "please use the help feature to get the usage of this command\nor use the bugreport feature if this is a false error");
            return;
        }

        String msg = String.join(" ", withoutFirst);

        spamMsg(msg, ctx.getChannel(), times);
    }

    void spamMsg(String msg, TextChannel channel, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            channel.sendMessage(msg).queue();
        }
    }

    void sendError(TextChannel channel, String title, String description) {
        channel.sendMessage(
                EmbedUtils.getDefaultEmbed().setTitle(title)
                        .setDescription(
                                new ColoredStringAsciiDoc()
                                        .addNormal(description)
                                        .build()
                        ).build()
        ).queue();
    }
}
