package JavaLauch.command.commands.admin;

import JavaLauch.Config;
import JavaLauch.command.CommandContext;
import JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;

public class OwnerPingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
       return;
    }

    public void handle(CommandContext ctx, boolean notAsCmd){
        final Member member = ctx.getMember();

        ctx.getChannel().sendMessage(
                EmbedUtils.getDefaultEmbed()
                        .setDescription("Shame on you " + member.getAsMention() + " for pinging my stupid boss")
                        .setImage(Config.get("png_path") + "getoutofmyroomimplaingWOW.png")
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "ownerping";
    }

    @Override
    public String getHelp() {
        return "Not a useful command just something that happens when my stupid boss gets pinged";
    }

    @Override
    public String getUsage() {
        return "this command isn't meant to be used";
    }
}
