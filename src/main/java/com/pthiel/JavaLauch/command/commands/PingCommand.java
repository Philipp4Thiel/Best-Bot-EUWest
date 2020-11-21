package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.Config;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle("Ping:")
                                .addField("Ping:"
                                        , ping + "ms"
                                        , true)
                                .addField("WS ping:"
                                        , jda.getGatewayPing() + "ms"
                                        , true)
                                .setFooter("requested by: " + ctx.getAuthor().getName())
                                .build()).queue()
        );
    }


    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public EmbedBuilder getHelp() {
        return new EmbedBuilder()
                .setTitle("Ping")
                .setDescription("It's a ping command what do you expect?")
                .addField("Usage:"
                        , "`" + Config.get("prefix") + "ping`"
                        , true);
    }
}
