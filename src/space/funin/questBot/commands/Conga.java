package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.concurrent.ExecutionException;

public class Conga  implements CommandExecutor {

    private static String topLeft = "<a:congaparrot:394097280560660483>";
    private static String topMiddle = "<a:congaparrot:393715754890952704>";
    private static String topRight = "<a:congaparrot:394097280644546560>";
    private static String bottomLeft = "<a:congaparrot:394097280086441985>";
    private static String bottomMiddle = "<a:congaparrot:394098379233296384>";
    private static String bottomRight = "<a:congaparrot:394097280292093953>";

    @Command(aliases = {"conga"}, async = true)
    public void onCall(ServerTextChannel channel) {
        System.out.println("entered conga line");
        try {
            channel.sendMessage(topLeft + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topMiddle + topRight).get();
            channel.sendMessage(bottomLeft + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomMiddle + bottomRight).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
