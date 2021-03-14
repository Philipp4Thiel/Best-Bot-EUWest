package BestBotEuWest.command.commands.owner;

import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.HashMap;
import java.util.List;

public class DrawCommand implements IOwnerCommand {

    final static HashMap<Integer, Integer> running = new HashMap<>();

    @Override
    public String getName() {
        return "draw";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args == null || args.isEmpty() || args.get(0).equals("draw")) {
            /*
             * TODO
             *  if image
             *      turn image into txt file
             *  create new thread (with low priority) that goes through each line in given txt file and keeps track of the count
             *  start thread
             */
            return;
        }
        if (args.get(1).equals("progress")){
            /*
             * TODO
             *  return for each element in [running] the current status
             *  using:
             *      :0percent:
             *      :25percent:
             *      :50percent:
             *      :75percent:
             *      :100percent:
             */
            return;
        }
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return null;
    }
}
