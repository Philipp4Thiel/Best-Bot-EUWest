package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.Config;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageFormat("Ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
        );

    }


    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "It's a ping command what do you expect\n" +
                "usage: '" + Config.get("prefix") + "ping'";
    }
}
