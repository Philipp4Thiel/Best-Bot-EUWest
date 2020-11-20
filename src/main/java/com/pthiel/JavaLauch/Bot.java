package com.pthiel.JavaLauch;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {

        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,

                GatewayIntent.DIRECT_MESSAGES
        )

                .addEventListeners(new Listener())
                .setActivity(Activity.listening("to crying children in the basement"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
