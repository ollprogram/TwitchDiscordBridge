

[//]: # (Copyright © 2025 ollprogram)

[//]: # ( This file is part of TwitchDiscordBridge.
TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or \(at your option\) any later version. 
TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
If not, see <https://www.gnu.org/licenses/>.)

# TwitchDiscordBridge

Links the [Discord](https://discord.com) chat and the [Twitch](https://www.twitch.tv) chat together.

## Author and Licence

Hi I'm ollprogram, the author of this project. Thanks for using it. **Feel free to report any bugs or typo. I'll try to fix them. Since english isn't my main language, feel free to correct me if I made any mistake in this documentation.**

Find information about the licence used for this project [here](LICENSE).

Familiarize yourself with the licence before using my project. It gives information about how you can use it.

## Description

With this application you will be able to link your Twitch chat with a Discord text channel in your discord server (guild).

When someone will send something on a specified channel on Discord, the same message will be sent to a specified Twitch chat, and reversely.

---

> In this project, I'm often referring to a "Bridge". But, what does a "Bridge" represent in this project?
> A "Bridge" is an object which transfers messages between two destinations. In our case destinations are Discord and Twitch.

## Download

You can download the runnable jar file [here](https://github.com/ollprogram/TwitchDiscordBridge/releases).

## Setup

There are some procedures before launching the program :

1. You need a [Discord](https://discord.com) account and a Discord server (guild).
2. Register a new bot on the [Discord Developer Portal](https://discord.com/developers/docs/intro) .  
   You should also turn it to private mode.
3. Add your bot on Discord with the [Discord Permissions Calculator](https://discordapi.com/permissions.html) using your bot client ID. The Discord bot will need some permissions on your server. See the permissions needed in the screenshot below:

   ![Discord Permissions](https://user-images.githubusercontent.com/39884051/148686617-3ca8d816-2a52-4724-a2d9-98dd1c962cb9.png)

   If you want to activate other permissions, you will have to think about consequences. For example, if you decide to activate mentions to all roles for the bot, then every person from Twitch will be able to mention all roles on Discord.

4. Create a new [Twitch](https://www.twitch.tv) account for your Twitch bot. Recommended to use OAuth2 and to make your bot a moderator on the Twitch channel where you stream.

---

When you launch the program for the first time, you need:

1. Your Discord bot token (grab it on the [Discord Dev Portal](https://discord.com/developers/docs/intro)).
2. Your Twitch bot user access token (grab it [here](https://twitchtokengenerator.com)).
3. A Twitch channel name (The channel where you stream).
4. A Discord channel ID. You need to activate first dev mode on Discord `advanced -> developer_mode -> on`. Right-click on the channel where you want your bot to listen to it, and copy the ID.

## Usage

### To launch the program with the console interface

`java -jar TwitchDiscordBridge-1.0-shaded.jar` (cmd or terminal)

### To list all commands

Type `help` or `<command> help` in the console. Or type `!help` on Discord.

### All commands on discord

- `/code` to get source code information.
- `/bridge discord_target <channel_id>` change the discord channel target
- `/bridge twitch_target <channel_name>` change the twitch channel target
- `/bridge open` open the bridge.
- `/bridge close` close the bridge.
- `/bridge info` get information about the bridge.

### All commands in the console

- `help` see all commands
- `shutdown` shutdowns the app without exceptions (wait all tasks end).
- `shutdown now` shutdowns the app with exceptions.
- `say your message` send a message on both platforms.
- `/code` to get source code information.
- `/bridge discord_target <channel_id>` change the discord channel target
- `/bridge twitch_target <channel_name>` change the twitch channel target
- `/bridge open` open the bridge.
- `/bridge close` close the bridge.
- `/bridge info` get information about the bridge.

## How to contribute?

[Here](CONTRIBUTING.md) are written information which you need in order to contribute to this project.

## Report a bug or something to improve



