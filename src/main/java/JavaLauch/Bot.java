package JavaLauch;

import JavaLauch.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private Bot() throws LoginException, SQLException, IOException {
        LOGGER.info("starting Bot");

        SQLiteDataSource.getConnection();
        LOGGER.info("started datasource");

        WebUtils.setUserAgent("Javabot by Lauch4P#2261");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(0xff00ff) //#ff00ff
                        .setFooter("powered by Lauch4P#2261 the most stupid of all bot owners")
        );

        LOGGER.info("starting JDA");
        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        )
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("ping me for help"))
                .build();
    }

    public static void main(String[] args) throws LoginException, SQLException, IOException {
        new Bot();
    }
}
