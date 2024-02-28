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

package fr.ollprogram.twitchdiscordbridge;

import org.jetbrains.annotations.NotNull;

public interface Bridge {

    /**
     * Shutdown the bots running
     */
    void shutdown();

    /**
     * Send to Twitch a message
     * @param message The message to send
     * @param channelId The channel id of the message
     */
    void sendToTwitch(@NotNull String message, @NotNull String channelId);

    /**
     * Send to Discord a message
     * @param message The message to send
     * @param channelId The channel id of the message
     */
    void sendToDiscord(@NotNull String message, @NotNull String channelId);
}
