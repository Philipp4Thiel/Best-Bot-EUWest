package JavaLauch.command.commands.owner;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class SetPresenceCommand implements IOwnerCommand {
    private final JDA bot;

    public SetPresenceCommand(JDA bot) {
        this.bot = bot;
    }

    @Override
    public String getName() {
        return "setPresence".toLowerCase();
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        List<String> args = ctx.getArgs();

        if (args == null || args.size() < 2) {
            missingArgs(ctx, "<type> <new presence>");
            return;
        }

        String type = args.get(0);

        String activity = String.join(" ", args.subList(1, args.size()));

        switch (type) {
            case "playing":
            case "p":
                bot.getPresence().setActivity(Activity.playing(activity));
                break;
            case "listening":
            case "l":
                bot.getPresence().setActivity(Activity.listening(activity));
                break;
            case "watching":
            case "w":
                bot.getPresence().setActivity(Activity.watching(activity));
                break;
            case "competing":
            case "c":
                bot.getPresence().setActivity(Activity.competing(activity));
                break;
        }

        ctx.getChannel().sendMessage("new presence set").queue();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("");

        // playing
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("new playing presence:")
                .addOrange(prefix + getName()+ " p <new presence>")
                .build(), false);

        // listening
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("new listening presence:")
                .addOrange(prefix + getName()+ " l <new presence>")
                .build(), false);

        // watching
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("new watching presence:")
                .addOrange(prefix + getName()+ " w <new presence>")
                .build(), false);

        // competing
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("new competing presence:")
                .addOrange(prefix + getName()+ " c <new presence>")
                .build(), false);

        return embed.build();
    }

    // Error Messages

    void missingArgs(CommandContext ctx, String arg) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                .setTitle("Args missing")
                .addField("", new ColoredStringAsciiDoc()
                        .addNormal("you're missing following args:")
                        .addOrange(arg)
                        .build(), false)
                .build()).queue();
    }
}
