package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
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
                                        , "```" + ping + "ms```"
                                        , true)
                                .addField("WS ping:"
                                        , "```" + jda.getGatewayPing() + "ms```"
                                        , true)
                                .build()
                ).queue()
        );
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
