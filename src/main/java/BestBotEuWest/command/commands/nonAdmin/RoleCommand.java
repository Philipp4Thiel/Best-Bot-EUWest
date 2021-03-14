package BestBotEuWest.command.commands.nonAdmin;

import BestBotEuWest.ColoredStrings.ColoredStringAsciiDoc;
import BestBotEuWest.command.CommandContext;
import BestBotEuWest.command.IPublicCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RoleCommand implements IPublicCommand {

    private final Map<String, List<Long>> roles;
    private final File role_json = new File("data/roles.json");

    public RoleCommand() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        roles = mapper.readValue(Files.readString(Path.of("data/roles.json")).replaceAll("\\R", "").replace(" ", ""),
                Map.class);
    }

    @Override
    public void handlePublic(CommandContext ctx) {
        List<String> args = ctx.getArgs();

        // no subcommand -> allRoles
        if (args == null || args.isEmpty()) {
            allRoles(ctx);
            return;
        }

        String subCommand = args.get(0);

        // subcommand: all
        if (subCommand.equals("all")) {
            allRoles(ctx);
            return;
        }

        // subcommand: signup
        if (subCommand.equals("signup")) {
            if (args.size() < 2) {
                notEnoughArgs(ctx, "role");
                return;
            }
            try {
                signUp(ctx, args.get(1));
            } catch (FileNotFoundException e) {
                errorWithJson(ctx);
            }
            return;
        }

        // subcommand: quit
        if (subCommand.equals("quit")) {
            if (args.size() < 2) {
                notEnoughArgs(ctx, "role");
                return;
            }
            try {
                quit(ctx, args.get(1));
            } catch (FileNotFoundException e) {
                errorWithJson(ctx);
            }
            return;
        }

        // subcommand: ping
        if (subCommand.equals("ping")) {
            if (args.size() < 2) {
                notEnoughArgs(ctx, "role");
                return;
            }
            ping(ctx, args.get(1));
            return;
        }

        // subcommand: <@member>
        if (subCommand.startsWith("<@!") && subCommand.endsWith(">")) {

            String id = subCommand.substring(3, 21);
            Member member = ctx.getMessage().getMentionedMembers().get(0);
            if (member == null) {
                userNotFoundError(ctx, subCommand);
                return;
            }
            if (!member.getId().equals(id)) {
                ctx.getChannel().sendMessage("something weird happened").queue();
                return;
            }
            allRolesOfMember(ctx, member);
            return;
        }
    }

    @Override
    public void handleAdmin(CommandContext ctx) {
        List<String> args = ctx.getArgs();

        // no subcommand -> no admin commands to handle
        if (args == null || args.isEmpty()) {
            handlePublic(ctx);
            return;
        }

        String subcommand = args.get(0);

        if (subcommand.equals("add")) {
            if (args.size() < 2) {
                notEnoughArgs(ctx, "role");
                return;
            }
            try {
                addRole(ctx, args.get(1));
            } catch (FileNotFoundException e) {
                errorWithJson(ctx);
            }
            return;
        }

        if (subcommand.equals("remove")) {
            if (args.size() < 2) {
                notEnoughArgs(ctx, "role");
                return;
            }
            try {
                removeRole(ctx, args.get(1));
            } catch (FileNotFoundException e) {
                errorWithJson(ctx);
            }
            return;
        }

        handlePublic(ctx);

    }

    @Override
    public void handleOwner(CommandContext ctx) {
        handleAdmin(ctx);
    }

    @Override
    public MessageEmbed getPublicHelp(String prefix) {
        return publicHelpRaw(prefix).build();
    }

    @Override
    public MessageEmbed getAdminHelp(String prefix) {
        EmbedBuilder embed = publicHelpRaw(prefix);

        // add
        embed.addField("",
                new ColoredStringAsciiDoc().addBlueAboveEq("Add a new role:")
                        .addNormal("With this you can add a new role.").addOrange(prefix + "role add <role>").build(),
                false);

        // remove
        embed.addField("",
                new ColoredStringAsciiDoc().addBlueAboveEq("Remove an existing role:")
                        .addNormal("With this you can remove an existing role.")
                        .addOrange(prefix + "role remove <role>").build(),
                false);

        return embed.build();
    }

    @Override
    public MessageEmbed getOwnerHelp(String prefix) {
        return getAdminHelp(prefix);
    }

    @Override
    public String getName() {
        return "role";
    }

    @Override
    public List<String> getAliases() {
        return List.of("roles", "ltp", "lfr", "lfg", "lookingtoplay");
    }

    // admin only
    void addRole(CommandContext ctx, String role) throws FileNotFoundException {
        if (roles.containsKey(role)) {
            roleAlreadyExistsError(ctx, role);
            return;
        }

        roles.putIfAbsent(role, new ArrayList<>());

        updateJson();

        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Success")
                .setDescription("Successfully added `" + role + "` to the available roles.").build()).queue();
    }

    // admin only
    void removeRole(CommandContext ctx, String role) throws FileNotFoundException {
        if (roles.containsKey(role)) {
            roles.remove(role);

            updateJson();

            ctx.getChannel()
                    .sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Success")
                            .setDescription("Successfully removed `" + role + "` from the available roles.").build())
                    .queue();
            return;
        }
        roleNotFoundError(ctx, role);
    }

    void signUp(CommandContext ctx, String role) throws FileNotFoundException {
        if (!roles.containsKey(role)) {
            roleNotFoundError(ctx, role);
            return;
        }

        if (roles.get(role).contains(ctx.getMember().getIdLong())) {
            alreadyHaveRoleError(ctx, role);
            return;
        }

        roles.get(role).add(ctx.getMember().getIdLong());

        updateJson();

        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Success")
                .setDescription("Successfully signed up for `" + role + "`.").build()).queue();
    }

    void quit(CommandContext ctx, String role) throws FileNotFoundException {
        if (!roles.containsKey(role)) {
            roleNotFoundError(ctx, role);
            return;
        }

        if (!roles.get(role).contains(ctx.getMember().getIdLong())) {
            youDontHaveThisRoleError(ctx, role);
            return;
        }

        roles.get(role).remove(ctx.getMember().getIdLong());

        updateJson();

        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Success")
                .setDescription("Successfully quited  `" + role + "`. :sadge:").build()).queue();
    }

    void ping(CommandContext ctx, String role) {
        if (!roles.containsKey(role)) {
            roleNotFoundError(ctx, role);
            return;
        }

        List<Long> memberIDs = roles.get(role);

        if (memberIDs == null || memberIDs.isEmpty()) {
            roleIsEmptyError(ctx, role);
            return;
        }

        if (!memberIDs.contains(ctx.getMember().getIdLong())) {
            youDontHaveThisRoleError(ctx, role);
            return;
        }

        StringBuilder allMentions = new StringBuilder().append("||");

        memberIDs.stream().forEach((it) -> allMentions.append("<@").append(it).append(">"));

        allMentions.append("||");

        ctx.getChannel().sendMessage(allMentions).queue();
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Get Pinged! :pinged:")
                .setDescription(role + " got pinged by: " + ctx.getMember().getAsMention())
                .addField("", "If you got wrongfully pinged submit a bug with the bug report feature.", false).build())
                .queue();
    }

    void allRoles(CommandContext ctx) {

        if (roles.keySet().isEmpty()) {
            thereAreNoRoles(ctx);
            return;
        }

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Here are all the roles");

        ColoredStringAsciiDoc allRoles = new ColoredStringAsciiDoc();

        roles.keySet().forEach(allRoles::addOrange);

        embed.addField("Roles:", allRoles.build(), false);

        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    void allRolesOfMember(CommandContext ctx, Member member) {
        Long id = member.getIdLong();

        ColoredStringAsciiDoc allRolesOfMember = new ColoredStringAsciiDoc();

        boolean hasRole = false;
        for (String role : roles.keySet()) {
            if (roles.get(role).contains(id)) {
                hasRole = true;
                allRolesOfMember.addOrange(role);
            }
        }

        if (!hasRole) {
            thisGuyDoesntHaveARole(ctx);
            return;
        }

        ctx.getChannel()
                .sendMessage(EmbedUtils.getDefaultEmbed()
                        .setTitle("Here are all the roles of " + member.getUser().getAsTag())
                        .addField("Roles:", allRolesOfMember.build(), false).build())
                .queue();
    }

    EmbedBuilder publicHelpRaw(String prefix) {
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Help page of: `" + getName() + "`");
        embed.setDescription(
                "A command to use the custom role feature of this bot so admins don't have to riot everytime we want new roles.");

        // general use
        embed.addField("",
                new ColoredStringAsciiDoc().addBlueAboveEq("general use:").addOrange(prefix + "role").build(), false);

        // all
        embed.addField("", new ColoredStringAsciiDoc().addBlueAboveEq("all roles:")
                .addNormal("With this you get a list of all roles available.").addOrange(prefix + "role all").build(),
                false);

        // signup
        embed.addField("", new ColoredStringAsciiDoc().addBlueAboveEq("sign up for a role:")
                .addNormal("With this you can sign up for a role.").addOrange(prefix + "role signup <role>").build(),
                false);

        // quit
        embed.addField("", new ColoredStringAsciiDoc().addBlueAboveEq("quit a role:")
                .addNormal("With this you can get rid of a role.").addOrange(prefix + "role quit <role>").build(),
                false);

        // ping
        embed.addField("",
                new ColoredStringAsciiDoc().addBlueAboveEq("ping a role:")
                        .addNormal("With this you can ping all members of a role.")
                        .addOrange(prefix + "role ping <role>").build(),
                false);

        // @member
        embed.addField("",
                new ColoredStringAsciiDoc().addBlueAboveEq("Inspect:")
                        .addNormal("With this you can see what roles somebody has.")
                        .addOrange(prefix + "role <@member>").build(),
                false);

        return embed;
    }

    void updateJson() throws FileNotFoundException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(roles);
            PrintStream out = new PrintStream(role_json);
            out.println(json);
            out.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Errors messages

    void roleNotFoundError(CommandContext ctx, String role) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("The role `" + role + "` was not found.").build()).queue();
    }

    void roleAlreadyExistsError(CommandContext ctx, String role) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("The role `" + role + "` does already exits.").build()).queue();
    }

    void roleIsEmptyError(CommandContext ctx, String role) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("The role `" + role + "` has no members.").build()).queue();
    }

    void youDontHaveThisRoleError(CommandContext ctx, String role) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("You don't have the role: `" + role + "`.").build()).queue();
    }

    void alreadyHaveRoleError(CommandContext ctx, String role) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("You already have the role: `" + role + "`.").build()).queue();
    }

    void thisGuyDoesntHaveARole(CommandContext ctx) {
        ctx.getChannel().sendMessage(
                EmbedUtils.getDefaultEmbed().setTitle("Error").setDescription("This user has no roles.").build())
                .queue();
    }

    void thereAreNoRoles(CommandContext ctx) {
        ctx.getChannel()
                .sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                        .setDescription(
                                "There are no roles on this server.\nAsk me or admins to add roles you'd like to have.")
                        .build())
                .queue();
    }

    void notEnoughArgs(CommandContext ctx, String missingArg) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error").setDescription(
                "There seems to be a missing argument.\nMake sure your command has a valid: `" + missingArg + "`")
                .build()).queue();
    }

    void userNotFoundError(CommandContext ctx, String user) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("Couldn't find user `" + user + "`.").build()).queue();
    }

    void errorWithJson(CommandContext ctx) {
        ctx.getChannel().sendMessage(EmbedUtils.getDefaultEmbed().setTitle("Error")
                .setDescription("something went wrong while writing into the jsonfile").build()).queue();
    }
}
