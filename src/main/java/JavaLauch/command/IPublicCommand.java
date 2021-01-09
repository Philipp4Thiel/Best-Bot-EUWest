package JavaLauch.command;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface IPublicCommand extends IAdminCommand {

    void handlePublic(CommandContext ctx);

    MessageEmbed getPublicHelp(String prefix);
}
