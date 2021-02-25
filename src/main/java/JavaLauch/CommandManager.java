package JavaLauch;

import JavaLauch.command.*;
import JavaLauch.command.commands.admin.SetPrefixCommand;
import JavaLauch.command.commands.nonAdmin.*;
import JavaLauch.command.commands.owner.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<IPublicCommand> publicCommands = new ArrayList<>();
    private final List<IAdminCommand> adminCommands = new ArrayList<>();
    private final List<IOwnerCommand> ownerCommands = new ArrayList<>();
    private final List<IDmCommand> dmCommands = new ArrayList<>();
    private final List<IHiddenCommand> hiddenCommands = new ArrayList<>();

    private final Set<ICommand> allCommands = new HashSet<>();

    public CommandManager(JDA bot) throws IOException {

        HelpCommand tempHelp = new HelpCommand(this);

        // adding commands visible to @everyone
        addPublicCommand(new PingCommand());
        addPublicCommand(tempHelp);
        addPublicCommand(new SuggestionsCommand());
        addPublicCommand(new BugReportCommand());
        addPublicCommand(new SpamCommand());
        addPublicCommand(new RoleCommand());
        addPublicCommand(new ReactCommand(bot));

        // adding commands visible to @admin
        addAdminCommand(new SetPrefixCommand());

        // adding commands visible to owner
        addOwnerCommand(new TestCommand());
        addOwnerCommand(new PrintInIDECommand());
        addOwnerCommand(new UpTimeCommand());
        addOwnerCommand(new ShutDownCommand());
        addOwnerCommand(new ReloadCommand());
        addOwnerCommand(new GetPFPCommand());
        addOwnerCommand(new SetPresenceCommand(bot));
        addOwnerCommand(new NickNameCommand());
        addOwnerCommand(new DeleteCommand());

        // adding commands usable in the dms of the bot
        addDmCommand(tempHelp);

        // adding hidden commands (like help on ping)
        addHiddenCommand(tempHelp);
    }

    private void addPublicCommand(IPublicCommand cmd) {
        addAdminCommand(cmd); // admins can use @everyone commands as well

        boolean nameFound = this.publicCommands.stream().anyMatch(
                (it) -> it.getName().equalsIgnoreCase(cmd.getName())
        );

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        publicCommands.add(cmd);
        addCommand(cmd);
    }

    public void addAdminCommand(IAdminCommand cmd) {
        addOwnerCommand(cmd); // owner can use @admin commands as well

        boolean nameFound = this.adminCommands.stream().anyMatch(
                (it) -> it.getName().equalsIgnoreCase(cmd.getName())
        );

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        adminCommands.add(cmd);
        addCommand(cmd);
    }

    public void addOwnerCommand(IOwnerCommand cmd) {
        boolean nameFound = this.ownerCommands.stream().anyMatch(
                (it) -> it.getName().equalsIgnoreCase(cmd.getName())
        );

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        ownerCommands.add(cmd);
        addCommand(cmd);
    }

    public void addDmCommand(IDmCommand cmd) {
        boolean nameFound = this.dmCommands.stream().anyMatch(
                (it) -> it.getName().equalsIgnoreCase(cmd.getName())
        );

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        dmCommands.add(cmd);
        addCommand(cmd);
    }

    public void addHiddenCommand(IHiddenCommand cmd) {
        boolean nameFound = this.hiddenCommands.stream().anyMatch(
                (it) -> it.getName().equalsIgnoreCase(cmd.getName())
        );

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        hiddenCommands.add(cmd);
        addCommand(cmd);
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {

        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        List<String> args = Arrays.asList(split).subList(1, split.length);

        CommandContext ctx = new CommandContext(event, args);

        int permissionLevel = ctx.getPermissionLevel();

        String cmdName = split[0].toLowerCase();

        switch (permissionLevel) {
            case 0:
                IPublicCommand publicCommand = searchPublicCommand(cmdName);
                if (publicCommand != null) {
                    publicCommand.handlePublic(ctx);
                }
                break;
            case 1:
                IAdminCommand adminCommand = searchAdminCommand(cmdName);
                if (adminCommand != null) {
                    adminCommand.handleAdmin(ctx);
                }
                break;
            case 2:
                IOwnerCommand ownerCommand = searchOwnerCommand(cmdName);
                if (ownerCommand != null) {
                    ownerCommand.handleOwner(ctx);
                }
                break;
        }
    }

    void handleHidden(String cmdName, GuildMessageReceivedEvent event, String prefix) {

        IHiddenCommand hiddenCommand = searchHiddenCommand(cmdName.toLowerCase());
        if (hiddenCommand != null) {
            event.getChannel().sendTyping().queue();
            CommandContext ctx = new CommandContext(event, null);
            hiddenCommand.handleHidden(ctx);
        }
    }

    public IOwnerCommand searchOwnerCommand(String search) {
        String searchLower = search.toLowerCase();

        for (IOwnerCommand cmd : ownerCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public IAdminCommand searchAdminCommand(String search) {
        String searchLower = search.toLowerCase();

        for (IAdminCommand cmd : adminCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public IPublicCommand searchPublicCommand(String search) {
        String searchLower = search.toLowerCase();

        for (IPublicCommand cmd : publicCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public IDmCommand searchDmCommand(String search) {
        String searchLower = search.toLowerCase();

        for (IDmCommand cmd : dmCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public IHiddenCommand searchHiddenCommand(String search) {
        String searchLower = search.toLowerCase();

        for (IHiddenCommand cmd : hiddenCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public List<IPublicCommand> getPublicCommands() {
        return publicCommands;
    }

    public List<IAdminCommand> getAdminCommands() {
        return adminCommands;
    }

    public List<IOwnerCommand> getOwnerCommands() {
        return ownerCommands;
    }

    public List<IDmCommand> getDmCommands() {
        return dmCommands;
    }

    public List<IHiddenCommand> getHiddenCommands() {
        return hiddenCommands;
    }

    public Set<ICommand> getAllCommands() {
        return allCommands;
    }

    private void addCommand(ICommand newCmd) {
        for (ICommand cmd :allCommands){
            if(newCmd.getClass().equals(cmd.getClass())){
                return;
            }
        }

        allCommands.add(newCmd);
    }
}




