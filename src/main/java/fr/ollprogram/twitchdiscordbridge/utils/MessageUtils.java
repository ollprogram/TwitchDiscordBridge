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

package fr.ollprogram.twitchdiscordbridge.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Message utility class
 */
public final class MessageUtils {

    /**
     * Can't be instantiated
     */
    private MessageUtils(){
        throw new UnsupportedOperationException("Message utils can't be instantiated");
    }

    /**
     * Filter the given message to avoid :
     * <ul>
     *     <li>Links, if they were disabled on one of the platform</li>
     * </ul>
     * <p>With this filter, this prevent users to bypass some permissions by using the bot chat.</p>
     * @param message The message to filter
     * @return The filtered message
     */
    public static String filterMessage(@NotNull String message){
        return message.replaceAll("(https://www\\.|http://www\\.|https://|http://)?[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?/[a-zA-Z0-9]{2,}|((https://www\\.|http://www\\.|https://|http://)?[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?)|(https://www\\.|http://www\\.|https://|http://)?[a-zA-Z0-9]{2,}\\.[a-zA-Z0-9]{2,}\\.[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,})?", "[url]");
    }
}
