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

package fr.ollprogram.twitchdiscordbridge.service;


import fr.ollprogram.twitchdiscordbridge.exception.ServiceException;
import fr.ollprogram.twitchdiscordbridge.model.TwitchBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Useful to request the twitch API without a heavy client for simple validation requests
 */
public interface TwitchService {

    /**
     * Authenticate to the app and get the bot info if authentication succeed, this operation is needed for other operations from the service.
     * @param token A valid app token
     * @return the bot info if authentication succeed
     */
    @NotNull Optional<TwitchBotInfo> authenticate(String token) throws ServiceException;


    /**
     * Retrieve the channel/user by its name
     * @param channelName The channel Name
     * @return The channel infos
     */
    @NotNull Optional<TwitchChannelInfo> getChannel(String channelName) throws ServiceException;
}
