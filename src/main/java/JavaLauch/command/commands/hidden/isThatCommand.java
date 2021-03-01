package JavaLauch.command.commands.hidden;

import JavaLauch.command.CommandContext;
import JavaLauch.command.IHiddenCommand;

public class isThatCommand implements IHiddenCommand {
    @Override
    public String getName() {
        return "isthat";
    }

    @Override
    public void handleHidden(CommandContext ctx) {
        ctx.getMessage().reply("definitely not a supra").queue();
    }
}
