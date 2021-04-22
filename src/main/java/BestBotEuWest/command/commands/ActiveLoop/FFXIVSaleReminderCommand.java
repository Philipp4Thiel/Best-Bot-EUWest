package BestBotEuWest.command.commands.ActiveLoop;

import BestBotEuWest.Listener;
import BestBotEuWest.command.IActiveLoopCommand;
import net.dv8tion.jda.api.entities.User;

public class FFXIVSaleReminderCommand implements IActiveLoopCommand {


    private static Listener listener;
    private static User owner;
    private static double lastPrice = 0.0;

    public FFXIVSaleReminderCommand(Listener listener) {
        FFXIVSaleReminderCommand.listener = listener;
    }

    @Override
    public void handleActive() {
        if (owner == null) {
            owner = listener.getOwner();
        }

        double curPrice = getPrice();
        if (curPrice < lastPrice) {
            owner.openPrivateChannel().complete().sendMessage("price for FFXIV droped from a total of " + lastPrice + " to a total of " + curPrice).queue();
        } else if (curPrice != lastPrice) {
            owner.openPrivateChannel().complete().sendMessage("price for FFXIV changed from a total of " + lastPrice + " to a total of " + curPrice).queue();
        }
        lastPrice = curPrice;
    }

    private double getPrice() {
        double price = 0.0;

        //TODO scrap FFXIV shop for prices if prices are lower then last time send msg to owner

        return Math.round(price * 100) / 100.0;
    }

    @Override
    public String getName() {
        return "ffxivsalereminder";
    }
}
