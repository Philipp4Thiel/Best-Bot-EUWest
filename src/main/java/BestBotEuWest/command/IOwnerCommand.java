package BestBotEuWest.command;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface IOwnerCommand extends ICommand{

    void handleOwner(CommandContext ctx);

    MessageEmbed getOwnerHelp(String prefix);
}
