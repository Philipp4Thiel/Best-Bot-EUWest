package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;

public class ReloadCommand implements IOwnerCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void handleOwner(CommandContext ctx){

        ctx.getChannel().sendMessage("reloading...").queue();

        final ArrayList<String> command = new ArrayList<String>();

        command.add("java");
        command.add("-jar");
        command.add("JavaLauch.jar");

        final ProcessBuilder builder = new ProcessBuilder(command);

        try {
            builder.start();
            ctx.getChannel().sendMessage("new instance should be running").queue();
            System.exit(0);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("couldn't reload").queue();
            ctx.getChannel().sendMessage(e.getClass().getName()).queue();
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
