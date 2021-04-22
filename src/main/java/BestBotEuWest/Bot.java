package BestBotEuWest;

import BestBotEuWest.data.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    private final Listener listener;

    private Bot() throws LoginException, SQLException {
        LOGGER.info("starting Bot");


        SQLiteDataSource.getConnection();
        LOGGER.info("started datasource");

        WebUtils.setUserAgent("Javabot by Lauch4P#2261");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(0xff00ff) //#ff00ff
                        .setFooter("powered by Lauch4P#2261", "https://cdn.discordapp.com/avatars/304587987956531201/08cb468af23e86a098bd72f3ba263829.png")
        );

        LOGGER.info("starting JDA");

        JDA jda = JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.playing("ping me for help"))
                .build();

        listener = new Listener(jda);

        jda.addEventListener(listener);
    }

    public static void main(String[] args) throws LoginException, SQLException, IOException, InterruptedException {
        Bot bot = new Bot();
        User owner = null;

        while (owner == null) {
            Thread.sleep(1000);
            owner = bot.listener.getOwner();
        }

        LOGGER.info("found owner " + owner.getAsTag());
        owner.openPrivateChannel().complete().sendMessage("found you").queue();
    }
}
