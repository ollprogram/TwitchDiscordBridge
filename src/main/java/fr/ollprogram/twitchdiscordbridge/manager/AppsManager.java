
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

package fr.ollprogram.twitchdiscordbridge.manager;

import fr.ollprogram.twitchdiscordbridge.command.CommandRegistry;
import fr.ollprogram.twitchdiscordbridge.command.TDBExecutor;
import org.jetbrains.annotations.NotNull;

public interface AppsManager {

    /**
     * Shutdown all the applications and the executor
     * @throws InterruptedException if interruption error occurs
     */
    void shutdownAll() throws InterruptedException;

    /**
     * Shutdown all the applications and the executor now
     * @throws InterruptedException if interruption error occurs
     */
    void shutdownAllNow() throws InterruptedException;

    /**
     * Check if the applications are all running
     * @return if the applications are all running
     */
    boolean areAllRunning();

    /**
     * Refresh the discord commands (should be called once at the beginning to avoid Rate limits)
     * @param registry The command Registry
     */
    void refreshDiscordCommands(CommandRegistry registry);

    /**
     * Get the command executor
     * @return The command executor
     */
    @NotNull TDBExecutor getExecutor();

}
