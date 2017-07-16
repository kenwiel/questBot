package space.funin.questBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

public class Quest {
    private long          questID;
    private String        roleID;
    private String        qmID;
    private String        name;
    private String        search;
    private String        description;
    private static Logger logger = LoggerFactory.getLogger(Quest.class);

    /**
     * @param roleID
     *            The RoleID of the Role belonging to the Quest
     * @param qmID
     *            The userID of the quest master
     * @param name
     *            The name of the Quest
     * @param search
     *            The title/searchterm of the Quest
     * @param decription
     *            A more detailed description of the quest
     */
    public Quest(String roleID, String qmID, String name, String search, String description) {
        setRole(roleID);
        setQM(qmID);
        setName(name);
        setSearch(search);
        setDescription(description);
        
        logger.info("Creating quest object");
        logger.info("RoleID: " + roleID);
        logger.info("qmID: " + qmID);
        logger.info("Questname: " + name);
        logger.info("Quest title: " + search);
        logger.info("Quest description: " + description);
    }
    
    public boolean isValid(Server server, Channel channel) {
    	if(roleID == null)
    		return false;
    	if(qmID == null)
    		return false;
    	if(name == null)
    		return false;
    	if(search == null)
    		return false;
    	
    	//make sure the role and QM still "exist"
    	try {
    		QuestBot.getRole(roleID, server).getName();
    	} catch (NullPointerException e){
    		channel.sendMessage("!!ranks : "+ name + " : " + CommandResponses.errorQuestRoleDoesNotExist);
    		channel.sendMessage("!!ranks: Removing Quest from Database. Please re-create it.");
    		Settings.removeMap(roleID, this);
    		return false;
    	}
    	try {
    		QuestBot.getUser(qmID).getName();
    	} catch (NullPointerException e){
    		channel.sendMessage("!!ranks : "+ name + " : " + CommandResponses.errorQuestUserDoesNotExist);
    		channel.sendMessage("!!ranks: Removing Quest from Database. Please re-create it.");
    		Settings.removeMap(roleID, this);
    		return false;
    	}
    	return true;
    }
    
    public Quest(String roleID, String qmID, String name, String search) {
        this(roleID, qmID, name, search, "None");
    }
    
    public long getQuestID() {
        return questID;
    }
    
    public String getRole() {
        return roleID;
    }
    
    // private because this should never be changed
    // roleID is the unique identifier of quests
    private void setRole(String roleID) {
        this.roleID = roleID;
    }
    
    public String getQM() {
        return qmID;
    }
    
    public void setQM(String qmID) {
        this.qmID = qmID;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSearch() {
        return search;
    }
    
    public void setSearch(String search) {
        this.search = search;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        User qm = QuestBot.getUser(qmID);
        return name + " by " + qm.getName();
    }
}
