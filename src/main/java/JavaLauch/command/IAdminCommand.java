package JavaLauch.command;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface IAdminCommand extends IOwnerCommand {

    void handleAdmin(CommandContext ctx);

    MessageEmbed getAdminHelp(String prefix);
}
