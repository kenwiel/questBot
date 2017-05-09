package space.funin.questBot.utils;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import space.funin.questBot.Quest;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;

public class CommandUtils {
    public static String[] combineCommands(String messageContent) {
        // remove the command at the beginning from the message
        messageContent = removeCommand(messageContent);
        
        // split by quotes
        String[] split = messageContent.split("\"");
        LinkedList<String> args = new LinkedList<String>(Arrays.asList(split));
        
        // remove empty (" ") arguments from in-between arguments
        // ( "arg1" "arg2" ) -> ("arg1"," ","arg2")
        while (args.contains(" ")) {
            args.remove(" ");
        }
        
        String[] arguments = args.toArray(new String[args.size()]);
        
        // remov whitespace
        for (int i = 0; i < arguments.length; i++)
            arguments[i] = arguments[i].trim();
        
        return arguments;
    }
    
    public static Role getMutedRole(Server server) {
    	Role muted = null;
		for (Role r : server.getRoles()) {
			if (r.getName().toLowerCase().equals("muted")) {
				muted = r;
				break;
			}
		}
		return muted;
    }
    
    public static boolean isMod(User user, Server server) {
    	List<Role> userRoles = new ArrayList<Role>(user.getRoles(server));
    	Permissions rolePermissions;
    	
    	for(Role r : userRoles) {
    		rolePermissions = r.getPermissions();
    		if(rolePermissions.getState(PermissionType.ADMINISTATOR).equals(PermissionState.ALLOWED))
    			return true;
    		if(rolePermissions.getState(PermissionType.MANAGE_SERVER).equals(PermissionState.ALLOWED))
    			return true;
    	}
    	return false;
    }
    
    public static void updateCache(Channel channel, boolean isEmbed) {
        String key = "**!!cache**";
        String value = "Refreshing cache...";
        if (isEmbed) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE).addField(key, value, true);
            
            try {
                channel.sendMessage("", eb).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(key + "\n").append(value);
            
            try {
                channel.sendMessage(sb.toString()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static String removeQuiet(String input) {
        for (String s : Settings.getQuiet()) {
            input = input.replace(s, "");
        }
        return input;
    }
    
    public static String[] splitFirstCommand(String[] args) {
        String[] split = args[0].split(" ");
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        
        return concatenate(split, newArgs);
    }
    
    private static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;
        
        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        
        return c;
    }
    
    public static String removeCommand(String input) {
        // space between command and arg0 -> remove first n characters from
        // input
        int firstSpace = input.indexOf(' ');
        String output = input.substring(firstSpace);
        
        return output;
    }
    
    public static void saveLoadResponse(boolean saved, Channel channel, boolean embed) {
        String name = saved ? "**__Manual save__**" : "**__Manual reload__**";
        String saveLoad = saved ? " saved " : " loaded ";
        int dbSize = Settings.getMap().size();
        String value = "Successfully" + saveLoad + dbSize + " Quests.";
        
        if (embed) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE).addField(name, value, true);
            
            try {
                channel.sendMessage("", eb).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name + "\n");
            sb.append(value);
            try {
                channel.sendMessage(sb.toString()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static List<String> getRoleIDs (List<Role> mentions) {

        List<String> roleIDList = new ArrayList<String>();
        
        for(Role role : mentions) {
            roleIDList.add(role.getId());
        }
        
        return roleIDList;
    }
    
    public static void questInfoCreator(Color color, Quest quest, Channel channel, Server server, boolean embed) {
        if (embed) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(color).setTitle("**__" + quest.getName() + "__**")
                    .addField("**Questmaster: **", QuestBot.getUser(quest.getQM()).getName(), true)
                    .addField("**Server role: **", QuestBot.getRole(quest.getRole(), server).getName(), true)
                    .addField("**Search string: **", quest.getSearch(), true)
                    .addField("**Description: **", quest.getDescription(), false);
            
            try {
                channel.sendMessage("", eb).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("**__" + quest.getName() + "__**")
                    .append("\n**Questmaster: **\n" + QuestBot.getUser(quest.getQM()).getName())
                    .append("\n**Server role: **\n" + QuestBot.getRole(quest.getRole(), server).getName())
                    .append("\n**Search string: **\n" + quest.getSearch())
                    .append("\n**Description: **\n" + quest.getDescription());
            try {
                channel.sendMessage(sb.toString()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean isEmbed(String[] args) {
        // if any of the arguments is a --quiet command, do not embed
        for (String s : Settings.getQuiet()) {
            for (String arg : args) {
                if (arg.equals(s)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String[] getArgs(Message message) {
        return message.getContent().split(" ");
    }
}
