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

package fr.ollprogram.twitchdiscordbridge.command;

import fr.ollprogram.twitchdiscordbridge.bridge.Bridge;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * A command to send a message on both applications (twitch and discord)
 */
public class Say extends Command {

    private static final String TEXT = "Message sent to both applications.";

    private static final String DESCRIPTION = "Send a message on both application (twitch and discord)";

    private static final String  MESSAGE_PREFIX = "Admin says : ";

    private final Bridge bridge;

    /**
     * Constructor
     * @param bridge The bridge
     */
    public Say(@NotNull Bridge bridge){
        super(DESCRIPTION, List.of(new Option("message", "The message to send", true)), true);
        this.bridge = bridge;
    }

    @Override
    public @NotNull Supplier<@NotNull String> getExecution(@NotNull List<@NotNull String> args) {
        if(validateArguments(args)){
            return () -> {
                String message = MESSAGE_PREFIX + args.parallelStream().reduce(" ", String::concat);
                bridge.sendToDiscord(message);
                bridge.sendToTwitch(message); //TODO fix exception if bridge is closed
                return TEXT;
            };
        }
        return () -> DEFAULT_ARGS_ERROR;
    }

}
