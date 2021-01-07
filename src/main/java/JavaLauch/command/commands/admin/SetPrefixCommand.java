package JavaLauch.command.commands.admin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.Config;
import JavaLauch.command.CommandContext;
import JavaLauch.command.ICommand;
import JavaLauch.data.PrefixMap;
import JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        long memberId = member.getIdLong();
        long ownerId = Long.parseLong(Config.get("owner_id"));

        // no perms
        if (!member.hasPermission(Permission.ADMINISTRATOR) && ownerId != memberId) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("ERROR");
            embed.setDescription(
                    new ColoredStringAsciiDoc()
                            .addNormal("you don't have the following permission:")
                            .addOrange("admin")
                            .build()
            );

            channel.sendMessage(embed.build()).queue();
            return;
        }

        // no prefix
        if (args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("ERROR");
            embed.setDescription(
                    new ColoredStringAsciiDoc()
                            .addNormal("your missing following args: ")
                            .addOrange("prefix")
                            .build()
            );

            channel.sendMessage(embed.build()).queue();
            return;
        }

        // update prefix
        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Updated Prefix");
        embed.setDescription(
                new ColoredStringAsciiDoc()
                        .addNormal("Updated prefix to:")
                        .addOrange(newPrefix)
                .build()
        );

        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public void handle(CommandContext ctx, boolean notAsCmd) {
        return;
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "a commands for admins to change the prefix";
    }

    @Override
    public String getUsage() {
        return "setprefix <newprefix>";
    }

    private void updatePrefix(long guildId, String newPrefix) {
        PrefixMap.PREFIXES.put(guildId, newPrefix);

        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                //language-SQLite
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}