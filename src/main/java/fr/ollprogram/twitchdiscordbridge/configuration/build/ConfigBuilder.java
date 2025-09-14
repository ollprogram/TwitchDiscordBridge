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
package fr.ollprogram.twitchdiscordbridge.configuration.build;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A builder for a configuration which allows you to put null values and or wrong values and change them before building
 * a concrete half immutable configuration.
 * It's not possible to build an incomplete configuration.
 * @author ollprogram
 */
public interface ConfigBuilder {

    /**
     * Set the discord channel id
     * @param id The discord channel id to set
     * @return This.
     */
    @NotNull ConfigBuilder setDiscordChannelID(String id);

    /**
     * Set the twitch token
     * @param token the twitch token to set
     * @return this
     */
    @NotNull ConfigBuilder setTwitchToken(String token);

    /**
     * Set the discord token
     * @param token the discord token
     * @return this
     */
    @NotNull ConfigBuilder setDiscordToken(String token);

    /**
     * Set the twitch channel name
     * @param name The channel name to set
     * @return this
     */
    @NotNull ConfigBuilder setTwitchChannelName(String name);

    /**
     * Get the twitch channel name
     * @return The twitch channel name, or null if not already provided
     */
    @Nullable String getTwitchChannelName();

    /**
     * Get the discord token
     * @return The discord token, or null if not already provided
     */
    @Nullable String getDiscordToken();

    /**
     * Get the twitch token
     * @return The twitch token, or null if not already provided
     */
    @Nullable String getTwitchToken();

    /**
     * Get the discord channel ID
     * @return The discord channel ID, or null if not already provided
     */
    @Nullable String getDiscordChannelID();

    /**
     * Check if all fields are all set to a non-null value.
     * @return If all fields are all set to a non-null value.
     */
    boolean isComplete();

    /**
     * Create the configuration
     * @return The concrete bridge config
     * @throws IllegalArgumentException If the number of fields is incomplete
     */
    @NotNull BridgeConfig build();
}
