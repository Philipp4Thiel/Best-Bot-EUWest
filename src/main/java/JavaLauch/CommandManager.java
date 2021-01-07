package JavaLauch;

import JavaLauch.command.commands.admin.OwnerPingCommand;
import JavaLauch.command.commands.admin.SetPrefixCommand;
import JavaLauch.command.commands.nonAdmin.*;
import JavaLauch.command.CommandContext;
import JavaLauch.command.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new TestCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new SuggestionsCommand());
        addCommand(new BugReportCommand());
        addCommand(new PrintInIDECommand());
        //addCommand(new OwnerPingCommand());
        addCommand(new SpamCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch(
                (it) -> it.getName().equalsIgnoreCase(cmd.getName())
        );

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }

    void handle(String cmdName, GuildMessageReceivedEvent event, String prefix) {

        String key = Config.get("random_key");

        if (!cmdName.startsWith(key)) {
            return;
        }

        cmdName = cmdName.replaceFirst(key, "");

        ICommand cmd = this.getCommand(cmdName);
        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            CommandContext ctx = new CommandContext(event, null);
            cmd.handle(ctx, true);
        }
    }
}




