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

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A command executor service. Based on the active object pattern to allow parallel execution of commands.
 */
public interface CommandExecutor {

    /**
     * Submit the command to the executor. The command will be executed.
     * @param command The command to execute.
     * @param args The arguments needed for the command execution
     * @return The command future.
     */
    @NotNull CompletableFuture<String> submit(Command command, List<String> args);

    /**
     * Shutdown the executor and delete all the pending command to execute. The executor can't receive any other commands after this call.
     */
    void shutdown();

    /**
     *
     * @return If the executor is shutdown.
     */
    boolean isShutdown();

}
