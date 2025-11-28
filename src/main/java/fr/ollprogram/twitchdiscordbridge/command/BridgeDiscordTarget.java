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

public class BridgeDiscordTarget extends Command{

    private static final String DESCRIPTION = "Change the discord target channel";
    private static final String CHANNEL_NOT_FOUND = "The channel doesn't exists or can't be retrieved.";

    private static final String CHANNEL_FOUND = "The target channel for discord has been changed.";

    private final Bridge bridge;
    public BridgeDiscordTarget(Bridge bridge) {
        super(DESCRIPTION, List.of(new Option("channel_id", "The discord target channel ID", true)), true);
        this.bridge = bridge;
    }

    @Override
    public @NotNull Supplier<String> getExecution(@NotNull List<String> args) {
        if(!validateArguments(args)) return () -> DEFAULT_ARGS_ERROR;
        String channelID = args.get(0);
        return () -> bridge.changeDiscordChannel(channelID) ? CHANNEL_FOUND : CHANNEL_NOT_FOUND;
    }
}
