package BestBotEuWest.NonCommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageManager.class);

    public MessageManager() {
    }

    public void handle(GuildMessageReceivedEvent event) {
        String guild = event.getGuild().getName();
        String channel = event.getChannel().getName();
        String member = event.getMember().getNickname();
        String content = event.getMessage().getContentRaw();

        LOGGER.info("\""+content + "\" in \"" + guild + "\" in \"" + channel + "\" from \"" + member + "\"");

        /*//create if not exist: MsgDB for guild exist
        try (final Statement statement = getConnection().createStatement()) {

            //language=SQLite
            statement.execute("CREATE TABLE IF NOT EXISTS MsgDB_allGuilds (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "guild_name VARCHAR(255) NOT NULL" +
                    ");");

            LOGGER.info("initialised MsgDB allGuilds");

        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        //create if not exist: MsgDB for channel exist
        //create if not exist: MsgDB for member exist

        //add content to member
        //update total in member

        //add member to channel
        //add content to channel
        //update total in channel

        //add channel to guild
        //add member to guild
        //add content to guild
        //update total in guild

        /*
        try (final Statement statement = getConnection().createStatement()) {

            //language=SQLite
            statement.executeUpdate("

            ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        return;
    }
}
