package JavaLauch;

import JavaLauch.data.PrefixMap;
import JavaLauch.data.SQLiteDataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    private final CommandManager manager;

    private SelfUser botUser;
    private long botUserID;
    private User owner;
    private final JDA bot;

    public Listener(JDA bot) throws IOException {
        this.bot = bot;
        manager = new CommandManager(bot);
        owner = bot.getUserById(Config.get("owner_id"));
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        botUser = event.getJDA().getSelfUser();
        botUserID = botUser.getIdLong();
        LOGGER.info("{} is ready", botUser.getAsTag());
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();

        if (user == botUser) {
            return;
        }

        if (owner == null && user.getId().equals(Config.get("owner_id"))) {
            owner = user;
            sendDM(owner, "linked you boss");
        } else if (owner == null) {
            owner = bot.getUserById(Config.get("owner_id"));
            if (owner == null) {
                sendDM(user, "sry dm aren't supported until i find my owner somewhere");
                return;
            }
        }

        if (user.getId().equals(Config.get("owner_id"))) {
            if (ownerDM(user, message)) {
                return;
            }
        }

        try {
            logDM(user.getAsTag(), message);
        } catch (IOException e) {
            LOGGER.info("couldn't't log DM");
        }

        LOGGER.info(user.getAsTag() + " DMed: " + message.getContentRaw());

        sendDM(owner, user.getAsMention() + " DMed me: \"" + message.getContentRaw() + "\"");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        if (event.getMessage().isTTS()) {
            event.getMessage().reply("i don't like tts ||" + event.getMember().getAsMention() + "||").queue();
        }

        final long guildId = event.getGuild().getIdLong();
        final String guildName = event.getGuild().getName();
        String prefix = PrefixMap.PREFIXES.computeIfAbsent(guildId, (id) -> getPrefix(guildId, guildName));
        String raw = event.getMessage().getContentRaw();

        //  bot gets pinged -> hidden help
        if (raw.equals("<@!" + botUserID + ">")) {
            manager.handleHidden("help", event, prefix);
        }

        // starts with prefix -> send to command handler
        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }

    User getOwner() {
        if (owner == null){
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

    void logDM(String user, Message message) throws IOException {
        Files.write(Paths.get("data/DMLog.txt"), (
                message.getTimeCreated() + " " + user + " DMed: " + message.getContentRaw() + "\n"
        ).getBytes(), StandardOpenOption.APPEND);
    }

    boolean ownerDM(User user, Message message) {
        String raw = message.getContentRaw();

        String prefix = "-";

        if (!raw.startsWith(prefix)) {
            return false;
        }

        List<String> args = Arrays.asList(message.getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+"));

        if (args.isEmpty()) {
            sendDM(user, "wtf that's not a cmd");
        }

        String cmd = args.get(0);

        if ("msg".equals(cmd)) {
            sendDMOrderedByOwner(args);
        }

        return true;
    }

    void sendDM(User user, String message) {
        user.openPrivateChannel().complete().sendMessage(message).queue();
    }

    void sendDMOrderedByOwner(List<String> args) {
        if (args.size() < 3) {
            sendDM(owner, "who/what?");
            return;
        }

        long id;

        try {
            id = Long.parseLong(args.get(1));
        } catch (NumberFormatException e) {
            sendDM(owner, "wtf that isn't even a long");
            return;
        }

        String msg = String.join(" ", args.subList(2, args.size()));

        User user;

        try {
            user = bot.getUserById(id);
        } catch (Exception e) {
            user = null;
            sendDM(owner, e.getMessage());
            return;
        }

        if (user == null) {
            sendDM(owner, "idk what happened but that user is kinda good at hiding");
            return;
        }

        sendDM(user, msg);
        sendDM(owner, "sent " + msg + " to " + user.getAsMention());
    }
}
