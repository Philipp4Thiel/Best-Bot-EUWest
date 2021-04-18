package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IHiddenCommand;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DrawCommand implements IOwnerCommand, IHiddenCommand {

    private TextChannel drawchannel = null;
    private Emote bar0 = null;
    private Emote bar25 = null;
    private Emote bar50 = null;
    private Emote bar75 = null;
    private Emote bar100 = null;

    final HashSet<DrawingTasks> running = new HashSet<>();

    @Override
    public String getName() {
        return "draw";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        List<String> args = ctx.getArgs();

        if (args != null && !args.isEmpty() && args.get(0).equalsIgnoreCase("linkChannel")) {
            drawchannel = ctx.getChannel();
            ctx.getMessage().reply("linked this channel to spam instructions in").queue();
            return;
        }

        if (args != null && !args.isEmpty() && args.get(0).equalsIgnoreCase("linkEmotes")) {
            List<Emote> emotes = ctx.getMessage().getEmotes();
            if (emotes.size() != 5) {
                ctx.getMessage().reply("can't find 5 emotes");
                return;
            }
            bar0 = emotes.get(0);
            bar25 = emotes.get(1);
            bar50 = emotes.get(2);
            bar75 = emotes.get(3);
            bar100 = emotes.get(4);
            ctx.getMessage().reply(String.format(
                    "Set %s as 0%% emote.%nSet %s as 25%% emote.%nSet %s as 50%% emote.%nSet %s as 75%% emote.%nSet %s as 100%% emote.%n",
                    bar0.getAsMention(), bar25.getAsMention(), bar50.getAsMention(), bar75.getAsMention(), bar100.getAsMention())).queue();
            return;
        }

        List<Attachment> attachments = ctx.getMessage().getAttachments();
        if (attachments.size() > 0) {
            int xOffset = 0;
            int yOffset = 0;

            try {
                assert args != null;
                xOffset = Integer.parseInt(args.get(0));
                yOffset = Integer.parseInt(args.get(1));
            } catch (Exception ignored) {
            }

            if (drawchannel == null) {
                ctx.getMessage().reply("link drawchannel first").queue();
                return;
            }
            Attachment attachment = attachments.get(0);
            if (attachment.isImage() || Objects.equals(attachment.getFileExtension(), "draw")) {
                if (attachment.isImage()) {
                    File image = null;
                    try {
                        image = attachment.downloadToFile("data/files/temp/" + attachment.getFileName()).join();
                        File instructions = turnIntoInstructions(image);
                        draw(instructions);
                        return;
                    } catch (IOException e) {
                        ctx.getMessage().reply("couldn't convert image to file").queue();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    } finally {
                        if (image != null) {
                            image.delete();
                        }
                    }
                }
                if (attachment.getFileExtension() != null && attachment.getFileExtension().equals("draw")) {
                    File instructions = attachment.downloadToFile("data/files/temp/" + attachment.getFileName()).join();
                    draw(instructions);
                    return;
                }
                ctx.getMessage().reply("bot will only parse images or .draw files for now").queue();
                return;
            }
        }

        StringBuilder msg = new StringBuilder();
        int counter = 1;
        for (DrawingTasks task : running) {
            msg.append(String.format("%2d. Task: %4.3f%% (%s)%n", counter++, task.getProgress() * 100, task.getDone()));
            if (bar0 != null && bar25 != null && bar50 != null && bar75 != null && bar100 != null) {
                int asInt = (int) (task.getProgress() * 40);
                int full = asInt / 4;
                String bar = "";
                bar += bar100.getAsMention().repeat(full);
                if (full < 10) {
                    int filler = asInt % 4;
                    switch (filler) {
                        case 0:
                            bar += bar0.getAsMention();
                            break;
                        case 1:
                            bar += bar25.getAsMention();
                            break;
                        case 2:
                            bar += bar50.getAsMention();
                            break;
                        case 3:
                            bar += bar75.getAsMention();
                            break;
                    }
                    if (full < 9) {
                        int empty = 9 - full;
                        bar += bar0.getAsMention().repeat(empty);
                    }
                }
                msg.append(bar).append("\n");
            }
        }
        if (msg.toString().equals("")) {
            ctx.getMessage().reply("no tasks running atm").queue();
            return;
        }
        ctx.getMessage().reply(msg + "||still need to implement live update||").queue();
        return;
    }


    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription("A command to print pictures in eth/place.");

        // general use
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("currently running tasks:")
                .addOrange(prefix + "draw")
                .build(), false);

        // link the channel
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("link channel:")
                .addOrange(prefix + "draw link")
                .build(), false);

        // start new task
        embed.addField("", new ColoredStringAsciiDoc()
                .addBlueAboveEq("start task:")
                .addOrange(prefix + "draw + attached image")
                .build(), false);

        return embed.build();
    }

    @Override
    public void handleHidden(CommandContext ctx) {
        String[] drawCommands = ctx.getMessage().getContentRaw().split("\n");
        int length = drawCommands.length - 1;

        Thread drawThread = new Thread(() -> {
            DrawingTasks task = new DrawingTasks(length);
            running.add(task);
            for (int i = 1; i < length; i++) {
                String line = drawCommands[i];
                if (line.equals("")) {
                    continue;
                }
                drawchannel.sendMessage(line).complete();
                task.incProgress();
            }
        });
        drawThread.setPriority(Thread.MIN_PRIORITY);
        drawThread.start();
    }

    private File turnIntoInstructions(File input) throws IOException {
        return turnIntoInstructions(input, 0, 0);
    }

    private File turnIntoInstructions(File input, int xOffset, int yOffset) throws IOException {
        String path = input.getPath() + ".draw";
        FileWriter output = new FileWriter(path);
        BufferedImage image = ImageIO.read(input);

        int width = image.getWidth();
        int height = image.getHeight();

        ArrayList<String> lines = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                if (alpha == 255) {
                    String instruction = String.format(".place setpixel %d %d #%s%s%s", x + xOffset, y + yOffset, Integer.toHexString(red), Integer.toHexString(green), Integer.toHexString(blue));
                    lines.add(instruction);
                }
            }
        }

        Collections.shuffle(lines);

        int size = lines.size();
        output.write("" + size);
        for (String line : lines) {
            output.write("\n" + line);
        }

        output.close();
        return new File(path);
    }

    private void draw(File instructions) {
        Thread drawThread = new Thread(() -> {
            DrawingTasks task = null;
            try (Scanner scanner = new Scanner(instructions)) {
                task = new DrawingTasks(scanner.nextInt());
                running.add(task);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equals("")) {
                        continue;
                    }
                    drawchannel.sendMessage(line).complete();
                    task.incProgress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running.remove(task);
                if (instructions != null) {
                    instructions.delete();
                }
            }
        });
        drawThread.setPriority(Thread.MIN_PRIORITY);
        drawThread.start();
    }

    static class DrawingTasks {

        final int total;
        int progress;

        DrawingTasks(int total) {
            this.total = total;
            progress = 0;
        }

        synchronized void incProgress() {
            progress++;
        }

        synchronized double getProgress() {
            return ((double) progress) / total;
        }

        public Object getDone() {
            return String.format("%d/%d", progress, total);
        }
    }
}
