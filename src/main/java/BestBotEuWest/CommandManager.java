package BestBotEuWest;

import BestBotEuWest.command.*;
import BestBotEuWest.command.commands.admin.SetPrefixCommand;
import BestBotEuWest.command.commands.nonAdmin.*;
import BestBotEuWest.command.commands.owner.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {
    private final Set<ICommand> allCommands = new HashSet<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager(JDA bot) {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new SuggestionsCommand());
        addCommand(new BugReportCommand());
        addCommand(new ReactCommand(bot));
        addCommand(new SetPrefixCommand());
        addCommand(new DrawCommand());
        addCommand(new TestCommand());
        addCommand(new PrintInIDECommand());
        addCommand(new UpTimeCommand());
        addCommand(new ShutDownCommand());
        addCommand(new ReloadCommand());
        addCommand(new GetPFPCommand());
        addCommand(new SetPresenceCommand(bot));
        addCommand(new NickNameCommand());
        addCommand(new DeleteCommand());
        addCommand(new SpamCommand());
        addCommand(new ActiveLoopCommand(this));
    }

    private void addCommand(ICommand cmd) {
        if (allCommands.stream().anyMatch((it) -> it.getClass().equals(cmd.getClass()))) {
            LOGGER.info(cmd.getName() + "is already in known commands");
            return;
        }

        allCommands.add(cmd);
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

        IHiddenCommand hiddenCommand = searchHiddenCommand(cmdName);
        if (hiddenCommand != null) {
            CommandContext ctx = new CommandContext(event, null);
            hiddenCommand.handleHidden(ctx);
        }
    }

    public IOwnerCommand searchOwnerCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : allCommands) {
            if (cmd instanceof IOwnerCommand) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return (IOwnerCommand) cmd;
                }
            }
        }

        return null;
    }

    public IAdminCommand searchAdminCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : allCommands) {
            if (cmd instanceof IAdminCommand) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return (IAdminCommand) cmd;
                }
            }
        }

        return null;
    }

    public IPublicCommand searchPublicCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : allCommands) {
            if (cmd instanceof IPublicCommand) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return (IPublicCommand) cmd;
                }
            }
        }

        return null;
    }

    public IDmCommand searchDmCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : allCommands) {
            if (cmd instanceof IDmCommand) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return (IDmCommand) cmd;
                }
            }
        }

        return null;
    }

    public IHiddenCommand searchHiddenCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : allCommands) {
            if (cmd instanceof IHiddenCommand) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return (IHiddenCommand) cmd;
                }
            }
        }

        return null;
    }

    public List<IPublicCommand> getPublicCommands() {
        List<IPublicCommand> toReturn = new LinkedList<>();
        for (ICommand cmd:allCommands){
            if (cmd instanceof IPublicCommand){
                toReturn.add((IPublicCommand) cmd);
            }
        }
        return toReturn;
    }

    public List<IAdminCommand> getAdminCommands() {
        List<IAdminCommand> toReturn = new LinkedList<>();
        for (ICommand cmd:allCommands){
            if (cmd instanceof IAdminCommand){
                toReturn.add((IAdminCommand) cmd);
            }
        }
        return toReturn;
    }

    public List<IOwnerCommand> getOwnerCommands() {
        List<IOwnerCommand> toReturn = new LinkedList<>();
        for (ICommand cmd:allCommands){
            if (cmd instanceof IOwnerCommand){
                toReturn.add((IOwnerCommand) cmd);
            }
        }
        return toReturn;
    }

    public List<IDmCommand> getDmCommands() {
        List<IDmCommand> toReturn = new LinkedList<>();
        for (ICommand cmd:allCommands){
            if (cmd instanceof IDmCommand){
                toReturn.add((IDmCommand) cmd);
            }
        }
        return toReturn;
    }

    public List<IHiddenCommand> getHiddenCommands() {
        List<IHiddenCommand> toReturn = new LinkedList<>();
        for (ICommand cmd:allCommands){
            if (cmd instanceof IHiddenCommand){
                toReturn.add((IHiddenCommand) cmd);
            }
        }
        return toReturn;
    }

    public Set<ICommand> getAllCommands() {
        return allCommands;
    }

    /*private void addCommand(ICommand newCmd) {
        for (ICommand cmd :allCommands){
            if(newCmd.getClass().equals(cmd.getClass())){
                return;
            }
        }

        allCommands.add(newCmd);
    }*/
}




