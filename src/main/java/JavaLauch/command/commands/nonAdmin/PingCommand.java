package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringDiff;
import JavaLauch.command.CommandContext;
import JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel().sendMessage(
                        EmbedUtils.getDefaultEmbed()
                                .setTitle(":parking::o2::ng:")
                                .addField("Ping:"
                                        , new ColoredStringDiff()
                                                .addRed(ping + "ms", true)
                                                .build()
                                        , true)
                                .addField("WS ping:"
                                        , new ColoredStringDiff()
                                        .addRed(jda.getGatewayPing() + "ms",true)
                                        .build()
                                        , true)
                                .build()
                ).queue()
        );
    }

    @Override
    public void handle(CommandContext ctx, boolean notAsCmd) {
        return;
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "It's a ping command what do you expect?";
    }

    @Override
    public String getUsage() {
        return "ping";
    }
}
