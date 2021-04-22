package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.CommandManager;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IActiveLoopCommand;
import BestBotEuWest.command.ICommand;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActiveLoopCommand implements IOwnerCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveLoopCommand.class);
    private static Thread activeLoop;
    private static CommandManager manager;

    public ActiveLoopCommand(CommandManager manager) {
        ActiveLoopCommand.manager = manager;
    }

    private static void activeLoop() throws InterruptedException {
        LOGGER.info("active loop started");

        for (ICommand cmd : manager.getAllCommands()) {
            if (cmd instanceof IActiveLoopCommand) {
                ((IActiveLoopCommand) cmd).handleActive();
                LOGGER.info("completed active loop of " + cmd.getName());
            }
        }

        LOGGER.info("active loop ended");
        Thread.sleep(10000L);
    }

    @Override
    public String getName() {
        return "activeloop";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args == null || args.isEmpty()) {
            ctx.getMessage().reply("no args given").queue();
            return;
        }
        String firstArg = args.get(0);
        switch (firstArg) {
            case "start":
                if (activeLoop == null || !activeLoop.isAlive()) {
                    activeLoop = new Thread(() -> {
                        while (true) {
                            try {
                                activeLoop();
                            } catch (InterruptedException e) {
                                LOGGER.info("active loop interrupted");
                                break;
                            }
                        }
                    });
                    activeLoop.start();
                    ctx.getMessage().reply("started active loop").queue();
                    return;
                }
                ctx.getMessage().reply("active loop already running").queue();
                return;
            case "stop":
                if (activeLoop != null && activeLoop.isAlive()) {
                    activeLoop.interrupt();
                    ctx.getMessage().reply("interrupted active loop").queue();
                    return;
                }
                ctx.getMessage().reply("active loop isn't running").queue();
                return;
            case "status":
                if (activeLoop != null && activeLoop.isAlive()) {
                    ctx.getMessage().reply("active loop is currently running").queue();
                    return;
                }
                ctx.getMessage().reply("active loop is currently not running").queue();
                return;
            default:
                ctx.getMessage().reply(firstArg + " is not a valid argument").queue();
        }
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("A command to control the activeLoop of the thread.");

        //start
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("start:")
                .addOrange(prefix + getName() + " start")
                .build(), false);

        //stop
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("stop:")
                .addOrange(prefix + getName() + " stop")
                .build(), false);

        //status
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("status:")
                .addOrange(prefix + getName() + " status")
                .build(), false);

        return embed.build();
    }
}
