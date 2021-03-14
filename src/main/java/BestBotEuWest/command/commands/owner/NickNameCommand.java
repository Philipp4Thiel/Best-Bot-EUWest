package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class NickNameCommand implements IOwnerCommand {

    @Override
    public String getName() {
        return "nick";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        String nickname = String.join(" ", ctx.getArgs());
        ctx.getGuild().getSelfMember().modifyNickname(nickname).queue();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("This command is used to change the nickname of the bot on this server.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc().addBlueAboveEq("general use:")
                .addOrange(prefix + "nick <new nickname>").build(), false);

        return embed.build();
    }
}
