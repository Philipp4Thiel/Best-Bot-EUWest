package com.pthiel.JavaLauch.command.commands.admin;

import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import com.pthiel.JavaLauch.data.PrefixMap;
import com.pthiel.JavaLauch.data.SQLiteDataSource;
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
        String prefix = PrefixMap.PREFIXES.get(ctx.getGuild().getIdLong());

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("ERROR");
            embed.setDescription("You must have the `manage server` permission to use this command");

            channel.sendMessage(embed.build()).queue();
            return;
        }

        if (args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

            embed.setTitle("ERROR");
            embed.setDescription("Missing args try `" + prefix + "help`");

            channel.sendMessage(embed.build()).queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Updated Prefix");
        embed.setDescription("updated prefix to `" + newPrefix + "`");

        channel.sendMessage(embed.build()).queue();
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
