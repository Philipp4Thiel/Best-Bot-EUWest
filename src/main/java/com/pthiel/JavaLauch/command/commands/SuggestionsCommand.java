package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import com.pthiel.JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SuggestionsCommand implements ICommand {


    @Override
    public void handle(CommandContext ctx) {

        List<String> args = ctx.getArgs();

        if (args.isEmpty()){
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("ERROR");
            embed.setDescription(
                    new ColoredStringAsciiDoc()
                            .addNormal("your missing following args: ")
                            .addOrange("suggestion")
                            .build()
            );

            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        String userName = ctx.getAuthor().getAsTag();
        String guildName = ctx.getGuild().getName();
        String suggestion = String.join(" ", args);

        try (final PreparedStatement insertStatement = SQLiteDataSource
                .getConnection()
                //language-SQLite
                .prepareStatement("INSERT INTO suggestions (user_name, guild_name, suggestion) VALUES (?, ?, ?)")) {

            insertStatement.setString(1, userName);
            insertStatement.setString(2, guildName);
            insertStatement.setString(3, suggestion);

            insertStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                .setTitle("Submitted suggestion")
                .setDescription(
                        new ColoredStringAsciiDoc()
                                .addOrange(suggestion)
                                .build()
                )
                .build()).queue();
    }

    @Override
    public String getName() {
        return "suggest";
    }

    @Override
    public String getHelp() {
        return "A command to make a suggestion (Stolen from Lukas xD)";
    }

    @Override
    public String getUsage() {
        return "suggest <suggestion>";
    }

    @Override
    public List<String> getAliases() {
        return List.of("suggestions", "suggestion");
    }
}
