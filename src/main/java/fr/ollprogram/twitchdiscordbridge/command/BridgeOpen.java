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

public class BridgeOpen extends Command{

    private static final String DESCRIPTION = "Open the bridge";

    private final Bridge bridge;
    public BridgeOpen(Bridge bridge) {
        super(DESCRIPTION, true);
        this.bridge = bridge;
    }

    @Override
    public @NotNull Supplier<String> getExecution(@NotNull List<String> args) {
        boolean isValid = validateArguments(args);
        if(!isValid) return () -> SHOULD_HAVE_NO_ARGS_ERROR;
        return () -> {
            if(bridge.isOpen()) return "Already opened !";
            bridge.open();
            return "The bridge is now open !";
        };
    }
}
