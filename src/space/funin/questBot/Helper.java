package space.funin.questBot;

import com.overzealous.remark.Remark;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import space.funin.questBot.Exceptions.RoleAlreadyPresentException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
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

    public static String replaceHtmlWithMarkdown(String input) {
        Remark remark = new Remark();
        String output = remark.convertFragment(input);

        output = output.replaceAll("[\r\n]+", "\n");
        output = output.replaceAll("\\h{2,}", " ");

        return output;
    }
}
