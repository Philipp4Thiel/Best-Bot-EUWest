package com.pthiel.JavaLauch;

import com.pthiel.JavaLauch.data.PrefixMap;
import com.pthiel.JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        final String guildName = event.getGuild().getName();
        String prefix = PrefixMap.PREFIXES.computeIfAbsent(guildId, (id) -> getPrefix(guildId, guildName));
        String raw = event.getMessage().getContentRaw();

        // Shutdown
        if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id"))) {
            LOGGER.info("shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }

    private String getPrefix(long guildId, String guildName) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                //language-SQLite
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatment = SQLiteDataSource
                    .getConnection()
                    //language-SQLite
                    .prepareStatement("INSERT INTO guild_settings (guild_id, guild_name) VALUES (?, ?)")) {

                insertStatment.setString(1, String.valueOf(guildId));
                insertStatment.setString(2, guildName);

                insertStatment.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefix");
    }
}
