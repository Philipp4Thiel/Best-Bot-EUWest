package BestBotEuWest.command.commands.nonAdmin;

import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IPublicCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.PriorityQueue;

public class RoleLBCommand implements IPublicCommand {
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
        return "rolelb";
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
        PriorityQueue<Member> LB = new PriorityQueue<>((o1, o2) -> {
            Integer score1 = o1.getRoles().size();
            Integer score2 = o2.getRoles().size();

            return score1.compareTo(score2);
        });

        LB.addAll(ctx.getGuild().loadMembers().get());
        Member cur = LB.poll();
        assert cur != null;
        StringBuilder msg = new StringBuilder(cur.getRoles().size() + ": " + cur.getAsMention());
        for (int i = 0; i < 9; i++) {
            if (LB.isEmpty()) break;
            cur = LB.poll();
            msg.append("\n").append(cur.getRoles().size()).append(": ").append(cur.getAsMention());
        }
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        embed.addField("most roles of the server:", msg.toString(), true);
        ctx.getMessage().reply(embed.build()).queue();
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        return null;
    }
}
