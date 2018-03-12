package space.funin.questBot;

import com.overzealous.remark.Remark;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import space.funin.questBot.Exceptions.RoleAlreadyPresentException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Helper {
    private Helper() {}

    /**
     * Adds a role to a user
     * @param server The server in which to add the role
     * @param user The user to add the role to
     * @param role The role to be added
     * @return The Future containing the update
     */
    public static CompletableFuture<Void> addRole(Server server, User user, Role role) throws RoleAlreadyPresentException {
        Collection<Role> userRoles = user.getRoles(server);

        if (userRoles.contains(role))
            throw new RoleAlreadyPresentException("The role " + role.getIdAsString() + " is already assigned to the user " + user.getIdAsString());

        userRoles.add(role);
        return server.updateRoles(user, userRoles);
    }

    /**
     * Checks whether the user has a moderator role in the chosen server
     * @param user The user to check
     * @param server The server to check
     * @return Whether the user has a role that contains "moderator" in its name
     */
    public static boolean isModerator(User user, Server server) {
        Collection<Role> userRoles = user.getRoles(server);
        /*if (user.isBotOwner())
            return true;*/
        return userRoles.stream().anyMatch(role -> role.getName().toLowerCase().contains("moderator"));
    }

    /**
     * Turns a string into a duration
     * @param input the string to convert
     * @return a duration with the specified length
     */
    private static Duration getDurationFromString(String input) throws DateTimeParseException {
        String modifiedInput = sanitizeDurationString(input);

        return Duration.parse(modifiedInput);
    }

    /**
     * Takes an array of strings and extracts a duration from one of them if possible
     * @param input the string array to look through
     * @return an optional of a duration with the length of the last convertable entry
     */
    public static Optional<Duration> getDurationFromString(String[] input) {
        Optional<Duration> converted = Optional.empty();
        for (String s : input) {
            try {
                converted = Optional.of(getDurationFromString(s));
            } catch (DateTimeParseException ignored) {
                //we're using optionals, we dont care if one string doesnt parse
            }
        }
        return converted;
    }

    /**
     * Cleans up an input string for use with Duration.parse
     * @param input the input to clean
     * @return the cleaned input
     */
    private static String sanitizeDurationString(String input) {
        String output = input.toUpperCase();

        //place T marker at correct position
        if (!output.contains("T")) {
            if (output.contains("D"))
                output = output.replace("D", "DT");
            else
                output = "T" + output;
        }
        //proper ISO time start
        if (!output.startsWith("P"))
            output = "P" + output;

        //purge spaces
        while (output.contains(" "))
            output = output.replace(" ", "");

        //replace min -> m
        while (output.contains("MIN"))
            output = output.replace("MIN", "M");

        //replace sec -> s
        while (output.contains("SEC"))
            output = output.replace("SEC", "S");

        //replace seconds -> s
        while (output.contains("SECONDS"))
            output = output.replace("SECONDS", "S");

        //if there is only a day duration, the string ends with DT, this would be illegal
        if (output.endsWith("T"))
            output = output.replace("T", "");

        //if no final time modifier is set, set it to minutes
        if (! (output.endsWith("D") || output.endsWith("H") || output.endsWith("M") || output.endsWith("S")))
            output += "M";

        return output;
    }

    /**
     * Replaces HTML character with discord markdown
     * @param input the HTML input to work on
     * @return the HTML formatted to markdown
     */
    public static String replaceHtmlWithMarkdown(String input) {
        Remark remark = new Remark();
        String output = remark.convertFragment(input);

        output = output.replaceAll("[\r\n]+", "\n");
        output = output.replaceAll("\\h{2,}", " ");

        return output;
    }

    /**
     * Pads a string with spaces to be left aligned and n characters long
     * @param s the string to pad
     * @param n the amount of characters its supposed to be long
     * @return the padded string
     */
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    /**
     * Pads a string with spaces to be right aligned and n characters long
     * @param s the string to pad
     * @param n the amount of characters its supposed to be long
     * @return the padded string
     */
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}
