package JavaLauch.command.commands.nonAdmin;

import JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import JavaLauch.command.CommandContext;
import JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;

import java.util.List;

public class SpamCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args == null || args.isEmpty()) {
            ctx.getChannel().sendMessage(
                    EmbedUtils.getDefaultEmbed().setTitle("Missing Args")
                            .setDescription(
                                    new ColoredStringAsciiDoc()
                                            .addNormal("use the help feature to get the usage of this command")
                                            .build()
                            ).build()
            ).queue();
            return;
        }
        String first = args.get(0);

        int times = 0;
        try {
            times = Integer.parseInt(first);
        } catch (NumberFormatException e) {
            ctx.getChannel().sendMessage(
                    EmbedUtils.getDefaultEmbed()
                            .setTitle("\"" + first + "\" is not a valid integer")
                            .setDescription(
                                    new ColoredStringAsciiDoc()
                                            .addNormal("use the help feature to get the usage of this command or report a bug if this is an error")
                                            .build()
                            )
                            .build()
            ).queue();
            return;
        }

        List<String> withoutFirst = args.subList(1, args.size());
        if (withoutFirst.isEmpty()) {
            ctx.getChannel().sendMessage(
                    EmbedUtils.getDefaultEmbed()
                            .setTitle("idk what happened but it shouldn't")
                            .setDescription(
                                    new ColoredStringAsciiDoc()
                                            .addNormal("please use the bugreport feature to report the bug")
                                            .build()
                            )
                            .build()
            ).queue();
            return;
        }
        String msg = String.join(" ", withoutFirst);
        for (int i = 0; i < times; i++) {
            ctx.getChannel().sendMessage(msg).queue();
        }
    }

    @Override
    public void handle(CommandContext ctx, boolean notAsCmd) {
        if (notAsCmd) {
            return;
        }
        this.handle(ctx);
    }

    @Override
    public String getName() {
        return "spam";
    }

    @Override
    public String getHelp() {
        return "spams given msg x times";
    }

    @Override
    public String getUsage() {
        return "spam <number of repetitions> <msg>";
    }

    @Override
    public List<String> getAliases() {
        return List.of("repeat");
    }
}
