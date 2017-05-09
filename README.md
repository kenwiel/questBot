# questBot
A Discord Bot for the managing of quests on 4chans /qst/ board

Main Discord Server: [!qtg](https://discordapp.com/invite/USPFgKa)
See !!help for commands
[Add the bot](https://discordapp.com/api/oauth2/authorize?client_id=302172063270174720&scope=bot&permissions=0) to your server

## Other projects used
* [JavaCh](https://github.com/camelKaiser/JavaCh) (edited)
* [Javacord](https://github.com/BtoBastian/Javacord)
* [Sdcf4j](https://github.com/BtoBastian/sdcf4j)

## Getting started
1. Create a mentionable role
2. Create the quest
  !!addQuest [@role] [@qm] ["quest name"] ["quest title"] <"quest description">  
  Users can now !!join or !!leave the role  
  When the role is mentioned, the bot will link to the latest thread on /qst/ with "quest title" in the subject field  

Some commands contain "special symbols"  
When [Square Brackets] surround an argument then that means the argument is mandatory for the command to work.  
When \<Pointed Brackets\> surround an argument, then that means the argument is optional.  

## Example
Create the role @TutorialQuest and make it mentionable  
!!addQuest @TutorialQuest @TutorialUser "Tutorial Quest" "Tutorial" "In Tutorial quest, you learn how to use the questBot!"  
!!join @TutorialQuest  

`@TutorialQuest`  
`questBot: TutorialQuest #1: link.to/thread`  
