# questBot
A Discord Bot for the managing of quests on 4chans /qst/ board

Main Discord Server: [!qtg](https://discordapp.com/invite/USPFgKa)  
See !!help for commands
[Add the bot to your server](https://discordapp.com/api/oauth2/authorize?client_id=302172063270174720&scope=bot&permissions=0)

## Other projects used
* [JavaCh](https://github.com/camelKaiser/JavaCh) (edited)  
* [Javacord](https://github.com/BtoBastian/Javacord)  
* [Sdcf4j](https://github.com/BtoBastian/sdcf4j)  

## Getting started  
1. Create a mentionable role (doable via !!createRole [roleName])  
2. Create the quest  
  !!addQuest [@role] [@qm] ["quest name"] ["quest title"] <"quest description">  
  Users can now !!join or !!leave the role to get notifications when it is @mentioned  
  When the role is mentioned, the bot will link to the latest thread on /qst/ with "quest title" in the subject field  



## Example  
Create the role @TutorialQuest  
  !!createRole TutorialQuest  

Create the Quest from that  
  !!addQuest @TutorialQuest @TutorialUser "Tutorial Quest" "Tutorial" "In Tutorial quest, you learn how to use the questBot!"  
  !!join @TutorialQuest  

  `@TutorialQuest`  
  `questBot: TutorialQuest #1: link.to/thread`  

## Full list of commands  
!!botinfo | Shows some information about the bot.  
!!alias [command] | Lists the aliases for a command  
!!help | Shows all available commands  
!!purge [amount] | Deletes the last x messages from the channel. Moderator only.  
!!cache | Manually update the qst cache  
!!newQuest [@role] [@qm] ["quest name"] ["search string"] <"quest description"> | Creates a new Quest.  
!!delQuest [@quest]\* | Removes a Quest. Moderator only.  
!!questInfo [@quest]\* | Gives additional info about a quest  
!!cr [roleName]\* | Creates a mentionable role for quest creation. Moderator only.  
!!removeRole [@role]\* | Removes role(s). Moderator Only.  
!!join [@mention]\* | Join the mentioned role(s)  
!!leave [@mention]\* | Leave the mentioned role(s)  
!!ranks | Outputs a list of ranks and their corresponding quests  
!!setupMute | Sets up Muted permissions on all channels  
!!mute [@user]* <Time> | Mutes a user (default 10 min). Moderator only.  
!!unmute [@user]* | Unmutes a user. Moderator only.  

### "Hidden" commands  
* !!save | Manually saves quest information to disk.  
* !!load | Manually loads quest information.(Useful when a thread gets mentioned just after creation, as it might not be cached yet).  
  
  
#### Moderator only commands require either:  
Administrator permission,  
Manage Server permission, or  
A role called "mod" or "moderator"  

#### Some commands contain "special symbols"  
When [Square Brackets] surround an argument then that means the argument is mandatory for the command to work.  
When \<Pointed Brackets\> surround an argument, then that means the argument is optional.  
When a star (\[arg\]\*) follows after an argument, then that means the command can take any amount of arguments of this kind  
