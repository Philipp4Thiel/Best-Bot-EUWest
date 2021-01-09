package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.IPublicCommand;
import JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SuggestionsCommand implements IPublicCommand {

    @Override
    public void handlePublic(CommandContext ctx) {

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
    public void handleOwner(CommandContext ctx) {
        this.handlePublic(ctx);
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName()+"`");
        embed.setDescription("A command to send a suggestion directly into my todolist.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("general use:")
                .addOrange(prefix + "suggest <suggestion>")
                .build(), false);

        return embed.build();    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        return null;
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return List.of("suggestions", "suggestion");
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        this.handlePublic(ctx);
    }
}
