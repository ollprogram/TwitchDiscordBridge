/* Copyright © 2024 ollprogram
 *
 * This file is part of TwitchDiscordBridge.
 * TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or \(at your option\) any later version.
 * TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
 * If not, see https://www.gnu.org/licenses.
 */
package fr.ollprogram.twitchdiscordbridge.configuration;

import org.jetbrains.annotations.NotNull;

/**
 * A simple implementation for a BridgeConfig
 * @author ollprogram
 */
public class BConf implements BridgeConfig {

    private final String twitchToken;
    private final String discordToken;

    private String discordChannelID;
    private String twitchChannelName;

    /**
     * Constructor
     * @param twitchChannelName The twitch channel name
     * @param discordChannelID The discord Channel ID
     * @param twitchToken The twitch bot token
     * @param discordToken The discord bot token
     */
    public BConf(@NotNull String twitchChannelName, @NotNull String discordChannelID,
                 @NotNull String twitchToken, @NotNull String discordToken) throws IllegalArgumentException {
        this.twitchToken = twitchToken;
        this.discordToken = discordToken;
        this.discordChannelID = discordChannelID;
        this.twitchChannelName = twitchChannelName;
    }

    @Override
    public @NotNull String getTwitchChannelName() {
        return twitchChannelName;
    }

    @Override
    public @NotNull String getDiscordChannelID() {
        return discordChannelID;
    }

    @Override
    public @NotNull String getTwitchToken() {
        return twitchToken;
    }

    @Override
    public @NotNull String getDiscordToken() {
        return discordToken;
    }

    @Override
    public void changeDiscordChannelID(@NotNull String id) {
        discordChannelID = id;
    }

    @Override
    public void changeTwitchChannelName(@NotNull String name) {
        twitchChannelName = name;
    }
}
