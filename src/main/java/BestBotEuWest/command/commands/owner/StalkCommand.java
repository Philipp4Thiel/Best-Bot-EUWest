package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class StalkCommand implements IOwnerCommand{

    @Override
    public String getName() {
        return "stalk";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        // TODO add user to stalk list 
        ctx.getMessage().reply("not yet implemented").queue();       
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("This is just a command made for stalking purposes.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "stalk <@user>")
                .build(), false);

        return embed.build();
    }
    
}