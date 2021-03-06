package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class DeleteCommand implements IOwnerCommand {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        if(ctx.getArgs() == null || ctx.getArgs().isEmpty()){
            return;
        }
        long id = Long.parseLong(ctx.getArgs().get(0));
        ctx.getMessage().delete().queue();
        ctx.getChannel().deleteMessageById(id).queue();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "delete <msgID>")
                .build(), false);

        return embed.build();
    }
}
