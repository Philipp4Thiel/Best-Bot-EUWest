package com.pthiel.JavaLauch.command.commands.nonAdmin;

import com.pthiel.JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import com.pthiel.JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BugReportCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        List<String> args = ctx.getArgs();

        if (args.isEmpty()){
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("ERROR");
            embed.setDescription(
                    new ColoredStringAsciiDoc()
                            .addNormal("your missing following args: ")
                            .addOrange("bug")
                            .build()
            );

            ctx.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        String userName = ctx.getAuthor().getAsTag();
        String guildName = ctx.getGuild().getName();
        String bug = String.join(" ", args);

        try (final PreparedStatement insertStatement = SQLiteDataSource
                .getConnection()
                //language-SQLite
                .prepareStatement("INSERT INTO bugreports (user_name, guild_name, bug) VALUES (?, ?, ?)")) {

            insertStatement.setString(1, userName);
            insertStatement.setString(2, guildName);
            insertStatement.setString(3, bug);

            insertStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed()
                .setTitle("Bugreport submitted")
                .setDescription(
                        new ColoredStringAsciiDoc()
                                .addOrange(bug)
                                .build()
                )
                .build()).queue();
    }

    @Override
    public void handle(CommandContext ctx, boolean notAsCmd) {
        return;
    }

    @Override
    public String getName() {
        return "bugreport";
    }

    @Override
    public String getHelp() {
        return "A command to send a bugreport";
    }

    @Override
    public String getUsage() {
        return "report <bug>";
    }

    @Override
    public List<String> getAliases() {
        return List.of("bug", "report");
    }
}
