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

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

public interface Command extends Callable<Void> {
    //TODO think about using a getCallable() method instead of implementing it the reason is that we wont be able to put arguments in it (in parallel execution) or we might clone the command
    //TODO asDiscordCommand with optional result
    /**
     * Get the command help
     * @return The manual of the command
     */
    @NotNull String getHelp();


}
