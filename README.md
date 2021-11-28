# TwitchDiscordBridge
Links the [Discord](https://discord.com) chat and the [Twitch](https://www.twitch.tv) chat together.
# Author and Licence :
Hi I'm ollprogram and I am the author of this project. Thanks for using it. </br>Please don't be afraid to report bugs or mistakes to me. I'll try to fix them. By the way, english isn't my main language, so I'm sorry for english mistakes but I'm open if you find some. </br>
Find information about the licence used for this project [here](https://github.com/ollprogram/TwitchDiscordBridge/blob/main/LICENSE).
You need to know the licence before using my project. It gives information about how you can use it.
# Description :
With this application you will be able to link your Twitch chat with a Discord text channel in a guild.
</br>When someone will send something on a specified channel on Discord, the same message will be send to a specified Twitch chat, and reversly.
</br>In this project i'm using a lot the word "Bridge". What does a "Bridge" represent in this project? 
</br>A "Bridge" is an object which transfers messages between two destinations. In our case destinations are Discord and Twitch.
# Dependencies :
For this project I'm using two APIs :
- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)
- [Twitch4J (Java Twitch API)](https://github.com/twitch4j/twitch4j)
# Download :
You can download the runnable jar file [here](https://github.com/ollprogram/TwitchDiscordBridge/releases/download/v1.0.0-beta/TwitchDiscordBridge-1.0-shaded.jar).
# Setup :
There are some procedures before launching the program :
1. You need a [Discord](https://discord.com) account and a Discord server (guild).
2. Register a new bot on the [Discord Dev Portal](https://discord.com/developers/docs/intro) (I recommand you to watch a tutorial on youtube).
3. Add your bot on Discord with the [Discord permission calculator](https://discordapi.com/permissions.html) using your bot client ID. The Discord bot will need some permissions on your server. See the permissions needed in the screenshot below :
<img src="https://user-images.githubusercontent.com/39884051/143782121-78b01135-d12f-474c-9239-8afa076120ef.png" width="600"></img>
4. Create a new <a href="https://www.twitch.tv">Twitch</a> account for your Twitch bot. Recommanded to use OAuth2 and to make your bot moderator on the twitch channel where you stream.
</br></br>
When you launch the program for the first time, you need :
1. Your Discord bot token (grab it on the <a href="https://discord.com/developers/docs/intro">Discord Dev Portal</a>).
2. Your Twitch bot token (grab it <a href="https://twitchtokengenerator.com">Here</a>).
3. A Twitch channel name (The channel where you stream).
4. A discord channel ID. You need to activate first dev mode on discord `advanced -> developer_mode -> on`. Right click on the channel where you want your bot to listen to it, and copy the ID.

# Usage :
To launch the program with the console interface : </br>
`java -jar TwitchDiscordBridge-1.0-shaded.jar` (cmd or terminal) </br>

To list all commands : </br>
Type `help` or `<command> help` in the console. Or type `!help` on Discord.

# JavaDocs :
Coming soon.

# Next Upates :
- Allow to lock the bridge on one side. (comming very soon)
