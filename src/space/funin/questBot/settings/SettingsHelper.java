package space.funin.questBot.settings;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

/**
 * This class handles saving and loading of all SettingsHelper for the bot.
 */
public class SettingsHelper {
    private static final  Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

    /**
     * This method reads the contents of a file.
     *
     * @param filePath The path to the file.
     * @param encoding The encoding of the file. Can be null. (Default UTF-8)
     * @return The contents of the file.
     */
    public static Optional<String> readFile(Path filePath, @Nullable Charset encoding) {
        if (encoding == null)
            encoding = DEFAULT_ENCODING;

        byte[] encoded;
        try {
            encoded = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(new String(encoded, encoding));
    }

    /**
     * This method writes a String to a file.
     *
     * @param filePath The path to the file.
     * @param content The content to be written.
     * @param encoding The encoding of the file. Can be null. (Default UTF-8)
     * @return Whether the file was written to successfully.
     */
    public static boolean writeFile(Path filePath, String content, @Nullable Charset encoding) {
        if (encoding == null)
            encoding = DEFAULT_ENCODING;

        try {
            Files.write(filePath, content.getBytes(encoding), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
