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
 * The Bridge configuration contains 4 information :
 * <ul>
 *     <li>The name of the twitch channel target (mutable) </li>
 *     <li>The id of the discord channel target (mutable) </li>
 *     <li>The discord token for the bot (immutable)</li>
 *     <li>The twitch token for the bot (immutable)</li>
 * </ul>
 * This configuration can be saved or read from other files easily.
 * This configuration contains all information needed for the two bots (twitch, discord) to run properly.
 * @author ollprogram
 */
public interface BridgeConfig {

    /**
     * The twitch channel name
     * @return The twitch channel name
     */
    @NotNull String getTwitchChannelName();

    /**
     * The discord channel ID
     * @return The discord channel ID
     */
    @NotNull String getDiscordChannelID();

    /**
     * Get the twitch bot token
     * @return The twitch bot token
     */
    @NotNull String getTwitchToken();

    /**
     * Get the discord bot token
     * @return The discord bot token
     */
    @NotNull String getDiscordToken();

    /**
     * Change the discord channel ID in this configuration
     * @param id The new discord channel ID
     */
    void changeDiscordChannelID(@NotNull String id);

    /**
     * Change the twitch channel name
     * @param name The new twitch channel name
     */
    void changeTwitchChannelName(@NotNull String name);

}

