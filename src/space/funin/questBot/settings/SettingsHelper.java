package space.funin.questBot.settings;

import com.google.gson.Gson;
import com.sun.istack.internal.Nullable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Optional;
import java.util.Set;

/**
 * This class handles saving and loading of all SettingsHelper for the bot.
 */
public class SettingsHelper {
    private static final  Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
    private static final FileAttribute<Set<PosixFilePermission>> FILE_ATTRIBUTE = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-"));

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
            FileOutputStream s = FileUtils.openOutputStream(filePath.toFile());

            byte[] contentInBytes = content.getBytes(encoding);

            s.write(contentInBytes);
            s.flush();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String loadToken() {
        File tokenFile = new File("./settings/token");
        if (tokenFile.exists()) {
            Optional<String> token = readFile(tokenFile.toPath(), null);
            if (token.isPresent())
                return token.get();
        }
        return null;
    }
}
