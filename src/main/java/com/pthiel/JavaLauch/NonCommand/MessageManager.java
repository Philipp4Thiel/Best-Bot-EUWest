package com.pthiel.JavaLauch.NonCommand;

import com.pthiel.JavaLauch.data.SQLiteDataSource;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageManager {

    public MessageManager() {
    }

    public void handle(GuildMessageReceivedEvent event){

        String content = event.getMessage().getContentRaw();
        Member member = event.getMember();
        Guild guild = event.getGuild();

        /*
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
        */

        return;
    }
}
