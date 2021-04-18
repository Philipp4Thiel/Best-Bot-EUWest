package BestBotEuWest.command;

import BestBotEuWest.Config;
import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

import static BestBotEuWest.command.PermissionLevel.*;

public class CommandContext implements ICommandContext {

    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final PermissionLevel permissionLevel;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
        Member member = this.getMember();

        permissionLevel = member.getId().equals(Config.get("owner_id")) ? OWNER : member.hasPermission(Permission.ADMINISTRATOR) ? ADMIN : PLEB;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }

    public PermissionLevel getPermissionLevel() {
        return this.permissionLevel;
    }
}

