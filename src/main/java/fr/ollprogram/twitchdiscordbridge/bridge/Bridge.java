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

package fr.ollprogram.twitchdiscordbridge.bridge;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the communication bridge between twitch and discord
 * In order to send a message, the bridge needs to be opened.
 */
public interface Bridge {

    /**
     * Check if the bridge is open
     * @return If the bridge is open
     */
    boolean isOpen();

    /**
     * Close the bridge
     */
    void close();

    /**
     * Open the bridge
     */
    void open();

    /**
     * Send to Twitch a message, the bridge should be opened first
     * @param message The message to send
     */
    void sendToTwitch(@NotNull String message);

    /**
     * Send to Discord a message, the bridge should be opened first
     * @param message The message to send
     */
    void sendToDiscord(@NotNull String message);

    /**
     * Send to discord a message, but bypass the bridge restrictions
     * @param message The message to send
     */
    void adminSendToDiscord(@NotNull String message);


    /**
     * Send to twitch a message, but bypass the bridge restrictions
     * @param message The message to send
     */
    void adminSendToTwitch(@NotNull String message);

    /**
     * Change the discord channel if it can be retrieved
     * @param channelID The new channel ID
     * @return If the channel exists and then has been changed
     */
    boolean changeDiscordChannel(@NotNull String channelID);

    /**
     * Change the twitch channel if it can be retrieved
     * @param channelName The new channel name
     * @return If the channel exists and then has been changed
     */
    boolean changeTwitchChannel(@NotNull String channelName);

    /**
     * Check if the given channel is the targeted channel
     * @param channelID The discord channel ID
     * @return if the given channel is the targeted channel
     */
    boolean isDiscordTarget(@NotNull String channelID);

    /**
     * Check if the given channel is the targeted channel
     * @param channelName The twitch channel ID
     * @return if the given channel is the targeted channel
     */
    boolean isTwitchTarget(@NotNull String channelName);

    /**
     * Get the bridge configuration
     * @return The bridge config
     */
    @NotNull BridgeConfig getConfig();

}
