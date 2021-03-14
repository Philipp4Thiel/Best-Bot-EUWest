package BestBotEuWest.command.commands.nonAdmin;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IPublicCommand;
import BestBotEuWest.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BugReportCommand implements IPublicCommand {
    @Override
    public void handlePublic(CommandContext ctx) {

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
    public String getName() {
        return "bugreport";
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("A command to report a bug. I will check them eventually, but if it is something really important (eg. permission exploit or stuff like this) feel free to ping or dm me.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "report <bug>")
                .build(), false);

        return embed.build();
    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return getPublicHelp(prefix);
    }

    @Override
    public List<String> getAliases() {
        return List.of("bug", "report");
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        this.handlePublic(ctx);
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        this.handlePublic(ctx);
    }
}
