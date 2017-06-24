package space.funin.questBot.Listeners.Bot;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.Settings;

public class BotAddResponseCommand implements CommandExecutor {
	
	@Command(aliases = { "!!addResponse", "!!ar" }, description = "Adds a response to the bots auto responder", usage = "!!addResponse [response]")
	public void onAddResponseCommand(String[] args, Channel channel) throws InterruptedException, ExecutionException {
		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			sb.append(s).append(" ");
		}
		String newResponse = sb.toString().trim();
		
		//Discord messages can only be 2000 characters
		//should never happen because it wouldnt send, but just to make sure
		if (newResponse.length() > 2000) {
			channel.sendMessage("!!addResponse : " + CommandResponses.errorMessageLength);
			return;
		}
		
		//otherwise, add the response
		List<String> responseList = Settings.loadMentionResponses();
		responseList.add(newResponse);
		Settings.saveMentionResponses(responseList);
		
		channel.sendMessage("!!addResponse : added Response to List.");
	}
}
