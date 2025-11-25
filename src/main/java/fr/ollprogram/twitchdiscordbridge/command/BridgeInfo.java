/*
 * Copyright © 2025 ollprogram
 * This file is part of TwitchDiscordBridge.
 * TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or \(at your option\) any later version.
 * TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
 * If not, see https://www.gnu.org/licenses.
 */

package fr.ollprogram.twitchdiscordbridge.command;

import fr.ollprogram.twitchdiscordbridge.bridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;


public class BridgeInfo extends Command {

    private static final String DESCRIPTION = "Show information about the bridge";

    private static final String INIT_TEXT = "Bridge information : ";

    private static final String ARGS_ERROR = "This command don't have any arguments.";

    private final Bridge bridge;

    private final JDA discordBot;

    public BridgeInfo(Bridge bridge, JDA discordBot){
        super(DESCRIPTION,true);
        this.bridge = bridge;
        this.discordBot = discordBot;
    }

    @Override
    public @NotNull Supplier<String> getExecution(@NotNull List<String> args) {
        if(validateArguments(args)) return this::getInformationMessage;
        else return () -> ARGS_ERROR;
    }

    private String getInformationMessage(){
        BridgeConfig config = bridge.getConfig();
        TextChannel discordChannel = discordBot.getTextChannelById(config.getDiscordChannelID());
        return INIT_TEXT + "\n```ansi\n" +
                "Status : " + (bridge.isOpen() ? "\u001b[0;32mOpened" : "\u001b[0;31mClosed") + "\u001b[0m\n"
                + "Twitch target channel : " + "\u001b[0;36m" + config.getTwitchChannelName() + "\u001b[0m\n"
                + "Discord target channel : " + (discordChannel == null ? "\u001b[0;31mNot found" : "\u001b[0;36m" + discordChannel.getName() ) + "\u001b[0m\n"
                + "```\n";
    }
}
