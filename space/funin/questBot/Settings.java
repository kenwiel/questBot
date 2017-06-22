package space.funin.questBot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class Settings {
	private static String directory = "./settings/";
	private static String[] quiet = { "--q", "-q", "--noEmbed", "--nE" };
	private static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
	private static Map<String, Quest> roleQuestMap = new HashMap<String, Quest>();
	private static String[] mentionResponses = loadMentionResponses();
	private static Logger logger = LoggerFactory.getLogger(Settings.class);

	public static String loadToken() {
		File tokenFile = new File(directory + "token");
		if (tokenFile.exists()) {
			String contents = readFile(tokenFile);
			Gson gson = new Gson();
			return gson.fromJson(contents, TypeTokens.STRING);
		}
		try {
			tokenFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeFile(tokenFile, "enter bot token here. This file should only contain \"token\", including the quotes.");
		return "";
	}

	public static String[] loadMentionResponses() {
		File responseFile = new File(directory + "responses");
		if (responseFile.exists()) {
			String contents = readFile(responseFile);
			Gson gson = new Gson();
			return gson.fromJson(contents, TypeTokens.ARRAY_STRING);
		} else {
			try {
				responseFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public static void saveMentionResponses(String[] mentionResponses) {
		File responseFile = new File(directory + "responses");
		if (responseFile.exists()) {
			String mentionResponsesGSON = new Gson().toJson(mentionResponses, TypeTokens.ARRAY_STRING);
			writeFile(responseFile, mentionResponsesGSON);
		} else {
			try {
				responseFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void addResponse(String newResponse) {
		   String[] temporary = new String[mentionResponses.length + 1];

		   for (int i = 0; i < mentionResponses.length; i++){
		      temporary[i] = mentionResponses[i];
		   }
		   temporary[mentionResponses.length+1] = newResponse;
		   saveMentionResponses(temporary);
		   loadMentionResponses();
	}

	public static void save() {
		saveMap(roleQuestMap);
	}

	public static void deleteQuest(String fileName) {
		File folder = new File(directory + "quests/");

		for (File file : folder.listFiles()) {
			// don't try to fuck with folders
			if (file.isFile() && file.getName().equals(fileName)) {
				file.delete();
			}
		}
	}

	public static void saveMap(Map<String, Quest> roleQuestMap) {
		Gson gson = new Gson();
		String questID = "";
		Quest quest;
		String questJSON = "";
		for (Entry<String, Quest> e : roleQuestMap.entrySet()) {
			questID = e.getKey();
			quest = e.getValue();
			questJSON = gson.toJson(quest, TypeTokens.QUEST);
			writeFile("quests/" + questID, questJSON);
		}

	}

	public static void reload() {
		roleQuestMap = new HashMap<String, Quest>();
		loadMap();
		mentionResponses = loadMentionResponses();
	}

	private static void loadMap() {
		File folder = new File(directory + "quests/");

		// make sure the folder to be loaded from in exists
		if (!folder.exists()) {
			logger.info("Folder(s) don't exist, creating folders.");
			folder.mkdirs();
		}

		for (File file : folder.listFiles()) {
			// don't try to fuck with folders
			if (file.isFile()) {
				logger.info("Found a file");
				putMap(file);
			}
		}
	}

	private static void putMap(File file) {
		String questID = file.getName();
		String contents = readFile(file);

		logger.info("Filename: " + questID);
		logger.info("File contents: " + contents);

		// if readFile(file) failed, the contents are ""
		// -> skip the entry
		if (contents.equals("")) {
			return;
		}
		putMap(contents);
	}

	private static void putMap(String contents) {
		Gson gson = new Gson();
		Quest quest = gson.fromJson(contents, TypeTokens.QUEST);
		quest = new Quest(quest.getRole(), quest.getQM(), quest.getName(), quest.getSearch(), quest.getDescription());

		putMap(quest.getRole(), quest);
	}

	public static void putMap(String roleID, Quest quest) {
		roleQuestMap.put(roleID, quest);
	}

	public static void removeMap(String roleID, Quest quest) {
		roleQuestMap.remove(roleID, quest);
	}

	public static String[] getMentionResponses() {
		return mentionResponses;
	}
	
	public static Map<String, Quest> getMap() {
		return roleQuestMap;
	}

	public static String[] getQuiet() {
		return quiet;
	}

	public static void setQuiet(String[] quiet) {
		Settings.quiet = quiet;
	}

	private static void writeFile(String filename, String contents) {
		File file = new File(directory + filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeFile(file, contents);
	}

	private static void writeFile(File file, String contents) {
		// make sure the folder to be saved in exists
		if (!file.getParentFile().exists()) {
			file.mkdirs();
		}

		try {
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut, CHARSET_UTF_8);
			myOutWriter.append(contents);
			myOutWriter.close();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String readFile(File file) {
		return readFile(file, CHARSET_UTF_8);
	}

	private static String readFile(File file, Charset charset) {
		return readFile(file.getPath(), charset);
	}

	private static String readFile(String path, Charset encoding) {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			String output = new String(encoded, encoding);
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
