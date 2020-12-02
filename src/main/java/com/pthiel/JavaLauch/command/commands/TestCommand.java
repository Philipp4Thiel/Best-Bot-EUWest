package com.pthiel.JavaLauch.command.commands;

import com.pthiel.JavaLauch.ColoredStrings.ColoredStringAsciiDoc;
import com.pthiel.JavaLauch.ColoredStrings.ColoredStringDiff;
import com.pthiel.JavaLauch.command.CommandContext;
import com.pthiel.JavaLauch.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;

public class TestCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage(
                EmbedUtils
                        .getDefaultEmbed()
                        .setTitle("Testing out the Embed/Code coloring features")
                        .addField("AsciiDoc Coloring"
                                , new ColoredStringAsciiDoc().addBlue("blue text")
                                        .addBlueAboveDash("blue text above dashes")
                                        .addBlueAboveDash("blue text above 4 dashes", 4)
                                        .addBlueAboveEq("blue text above equals")
                                        .addBlueAboveEq("blue text above 3 equals", 3)
                                        .addNormal("normal text")
                                        .addOrange("orange text")
                                        .build()
                                , true)
                        .addField("Diff Coloring"
                                , new ColoredStringDiff()
                                        .addGreen("unsymmetrical green")
                                        .addGreen("specified unsymmetrical green", false)
                                        .addGreen("symmetrical green", true)
                                        .addRed("unsymmetrical red")
                                        .addRed("specified unsymmetrical red", false)
                                        .addRed("symmetrical red", true)
                                        .addNormal("normal white text")
                                        .addGrayDashes("unsymmetrical gray dashes")
                                        .addGrayDashes("specified unsymmetrical gray dashes", false)
                                        .addGrayDashes("symmetrical gray dashes", true)
                                        .addGrayTimes("unsymmetrical gray times")
                                        .addGrayTimes("specified unsymmetrical gray times", false)
                                        .addGrayTimes("symmetrical gray times", true)
                                        .build()
                                , true)
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return "just a command to test stuff. But this description needs to be really long for testing purposes so you can just ignore the shit that is written in here.";
    }

    @Override
    public String getUsage() {
        return "test";
    }
}
