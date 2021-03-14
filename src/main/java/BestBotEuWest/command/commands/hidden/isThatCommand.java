package BestBotEuWest.command.commands.hidden;

import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IHiddenCommand;
import net.dv8tion.jda.api.entities.Message;

public class isThatCommand implements IHiddenCommand {
    @Override
    public String getName() {
        return "isthat";
    }

    @Override
    public void handleHidden(CommandContext ctx) {
        Message msg = ctx.getMessage().reply(".").complete();
        msg.editMessage("definitely not a supra").queue();
    }
}
