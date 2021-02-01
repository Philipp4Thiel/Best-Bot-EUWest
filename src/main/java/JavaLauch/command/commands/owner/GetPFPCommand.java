package JavaLauch.command.commands.owner;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class GetPFPCommand implements IOwnerCommand {
    @Override
    public String getName() {
        return "getpfp";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        List<Member> mentions = ctx.getMessage().getMentionedMembers();
        if (mentions == null || mentions.isEmpty()) {
            ctx.getChannel().sendMessage("pls mention a user").queue();
            return;
        }
        StringBuilder pfp = new StringBuilder();

        for (Member member : mentions) {
            pfp.append("\n").append(member.getUser().getAvatarUrl());
        }
        try {
            ctx.getMessage().delete().queue();
        } catch (InsufficientPermissionException e){
        }

        ctx.getChannel().sendMessage(pfp.toString()).queue();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "getpfp <@user>")
                .build(), false);

        return embed.build();
    }
}
