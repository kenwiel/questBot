package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import javach.Post;
import space.funin.questBot.Helper;
import space.funin.questBot.QuestBot;

import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

public class BrowseQst implements CommandExecutor {

    private final String leftArrow = "\u2B05";
    private final String rightArrow = "\u27A1";

    @Command(aliases = {"browse"},  async = true)
    public void onCall(Server server, ServerTextChannel channel, String[] args) {
        int threadId = Integer.parseInt(args[0]);

        javach.Thread thread = QuestBot.getQst().getThread(threadId, true);
        ListIterator<Post> iterator = thread.getPosts().listIterator();

        try {
            Message browser = channel.sendMessage(null, postFormatter(iterator.next())).get();

            browser.addReaction(leftArrow);
            browser.addReaction(rightArrow);
            browser.addReactionAddListener(reactionAddEvent -> {
                if(reactionAddEvent.getUser().isBot())
                    return;

                switch (reactionAddEvent.getEmoji().asUnicodeEmoji().get()) {
                    default:
                        break;
                    case leftArrow:
                        reactionAddEvent.removeReaction();
                        if (iterator.hasPrevious()) {
                            reactionAddEvent.getMessage().get().edit(postFormatter(iterator.previous()));
                        }
                        break;
                    case rightArrow:
                        reactionAddEvent.removeReaction();
                        if (iterator.hasNext()) {
                            reactionAddEvent.getMessage().get().edit(postFormatter(iterator.next()));
                        }
                        break;
                }
            });
        } catch (InterruptedException | ExecutionException ignored) {
            //who gives a fuck
        }
    }

    private EmbedBuilder postFormatter(javach.Post post) {
        EmbedBuilder eb = new EmbedBuilder();
        String name = post.getPosterName() + " (" + post.getPosterID() + ")";

        eb.setAuthor(name, post.url(), null);
        if(post.hasFile())
            eb.setThumbnail(post.getFile().url());
        eb.setTitle(post.subject());
        eb.setDescription(Helper.replaceHtmlWithMarkdown(post.getText()));

        return eb;
    }
}
