package BestBotEuWest.command.commands.ActiveLoop;

import BestBotEuWest.CommandManager;
import BestBotEuWest.Listener;
import BestBotEuWest.command.IActiveLoopCommand;
import net.dv8tion.jda.api.entities.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FFXIVSaleReminderCommand implements IActiveLoopCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

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

        double curPrice;
        try {
            curPrice = getPrice();
        } catch (IOException e) {
            LOGGER.error("couldn't fetch price of FFXIV:" + e.getMessage());
            return;
        }
        if (curPrice < lastPrice) {
            owner.openPrivateChannel().complete().sendMessage("price for FFXIV droped from a total of " + lastPrice + " to a total of " + curPrice).queue();
        } else if (curPrice != lastPrice) {
            owner.openPrivateChannel().complete().sendMessage("price for FFXIV changed from a total of " + lastPrice + " to a total of " + curPrice).queue();
        }
        lastPrice = curPrice;
    }

    private double getPrice() throws IOException {
        double price = 0.0;

        //TODO scrap FFXIV shop for prices if prices are lower then last time send msg to owner

        //Story skip shop
        String url = "https://store.finalfantasyxiv.com/ffxivstore/en-gb/category/0";
        Document shop = Jsoup.connect(url).get();
        Elements pageElements = shop.select(".item-card-stretch");
        System.out.println(">"+pageElements+"<");
        System.out.println(shop);


        return Math.round(price * 100) / 100.0;
    }

    @Override
    public String getName() {
        return "ffxivsalereminder";
    }
}
