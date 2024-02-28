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

package fr.ollprogram.twitchdiscordbridge.configuration.validate;

/**
 * The validator of a bridge configuration.
 *
 * @author ollprogram
 */
public interface BridgeConfigValidator {

    /**
     * Load the configuration.
     * While loading, the configuration may change if the channels specified are wrong.
     * Default values are :
     * <ul>
     *     <li>Twitch channel : bot own channel</li>
     *     <li>Discord channel : default guild channel or discord system private channel</li>
     * </ul>
     */
    void loadConfiguration();

    /**
     * Check if the configuration has been validated.
     * @return If the configuration is valid.
     * A configuration is valid when the bot tokens are valid and if the channels exists.
     */
    boolean isValid();
}
