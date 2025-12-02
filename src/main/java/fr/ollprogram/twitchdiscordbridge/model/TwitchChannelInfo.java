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

package fr.ollprogram.twitchdiscordbridge.model;

import org.jetbrains.annotations.NotNull;

/**
 * A record to simply represent a twitch channel
 * @param id The twitch channel ID
 * @param channelName The twitch channel name
 */
public record TwitchChannelInfo(@NotNull String id, @NotNull String channelName) {
    @Override
    public String toString() {
        return "{" +
                "ChannelID='" + id + '\'' +
                ", ChannelName='" + channelName + '\'' +
                '}';
    }
}
