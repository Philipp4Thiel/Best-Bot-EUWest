package com.pthiel.JavaLauch;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {
        WebUtils.setUserAgent("Javabot by Lauch4P#0001");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                .setColor(0x307f1a)
                .setFooter("JavaLauch")
        );

        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        )
                .addEventListeners(new Listener())
                .setActivity(Activity.listening("to crying children in the basement"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
