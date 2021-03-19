package BestBotEuWest.command.commands.owner;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IOwnerCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DrawCommand implements IOwnerCommand {

    private TextChannel drawchannel = null;

    final HashSet<DrawingTasks> running = new HashSet<>();

    @Override
    public String getName() {
        return "draw";
    }

    @Override
    public void handleOwner(CommandContext ctx) {
        List<String> args = ctx.getArgs();

        if (args != null && !args.isEmpty() && args.get(0).equals("link")) {
            drawchannel = ctx.getChannel();
            ctx.getMessage().reply("linked this channel to spam instructions in").queue();
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
        /*
         * TODO
         *  return for each element in [workingOn] the current status
         *  using these emotes:
         *      :0percent:
         *      :25percent:
         *      :50percent:
         *      :75percent:
         *      :100percent:
         */
        StringBuilder msg = new StringBuilder();
        int counter = 1;
        for (DrawingTasks task : running) {
            msg.append(String.format("%2d. Task: %4.3f%%%n", counter++, task.getProgress() * 100));
        }
        if (msg.toString().equals("")) {
            ctx.getMessage().reply("no tasks running atm").queue();
            return;
        }
        ctx.getMessage().reply(msg).queue();
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
    }
}
