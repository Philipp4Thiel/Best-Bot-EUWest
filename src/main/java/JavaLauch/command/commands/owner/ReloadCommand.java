package JavaLauch.command.commands.owner;

import JavaLauch.command.CommandContext;
import JavaLauch.command.IOwnerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ReloadCommand implements IOwnerCommand {

    // TODO
    //  getName
    //  handleOwner
    //  getUsage
    //  getOwnerHelp

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void handleOwner(CommandContext ctx) {

    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return null;
    }
}
