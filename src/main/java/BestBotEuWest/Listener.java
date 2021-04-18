package BestBotEuWest;

import BestBotEuWest.data.PrefixMap;
import BestBotEuWest.data.SQLiteDataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.SelfUser;
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

    private final CommandManager manager;

    private SelfUser botUser;
    private long botUserID;
    private User owner;
    private final JDA bot;

    public Listener(JDA bot) {
        this.bot = bot;
        manager = new CommandManager(bot);
        owner = bot.getUserById(Config.get("owner_id"));
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        botUser = event.getJDA().getSelfUser();
        botUserID = botUser.getIdLong();
        LOGGER.info("{} is ready (id:{})", botUser.getAsTag(), botUserID);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        User user = event.getAuthor();
        final long guildId = event.getGuild().getIdLong();
        final String guildName = event.getGuild().getName();
        String prefix = PrefixMap.PREFIXES.computeIfAbsent(guildId, (id) -> getPrefix(guildId, guildName));
        String raw = event.getMessage().getContentRaw();

        if (user.isBot() || event.isWebhookMessage()) {
            if (event.getAuthor().getId().equals("590453186922545152") && raw.contains("attack these pixels!")
                    && raw.contains(botUser.getId())){
                manager.handleHidden("draw", event, prefix);
            }
            return;
        }

        //  bot gets pinged -> hidden help
        if (raw.equals("<@!" + botUserID + ">") || raw.equals("<@" + botUserID + ">")) {
            manager.handleHidden("help", event, prefix);
        }

        // starts with prefix -> send to command handler
        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }

    User getOwner() {
        if (owner == null) {
            return bot.getUserById(Config.get("owner_id"));
        }
        return owner;
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

            try (final PreparedStatement insertStatement = SQLiteDataSource
                    .getConnection()
                    //language-SQLite
                    .prepareStatement("INSERT INTO guild_settings (guild_id, guild_name) VALUES (?, ?)")) {

                insertStatement.setString(1, String.valueOf(guildId));
                insertStatement.setString(2, guildName);

                insertStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefix");
    }
}
