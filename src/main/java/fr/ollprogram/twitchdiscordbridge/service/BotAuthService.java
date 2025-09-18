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

import fr.ollprogram.twitchdiscordbridge.service.model.BotInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface BotAuthService {

    /**
     * Authenticate to the app and get the bot info if authentication succeed
     * @param token A valid app token
     * @return the bot info if authentication succeed
     */
    @NotNull Optional<BotInfo> authenticate(String token);

}
