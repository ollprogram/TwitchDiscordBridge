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

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * A registry of usable commands.
 */
public interface CommandRegistry {

    /**
     * Register a command to the registry.
     * @param command The command to register.
     */
    void register(String name, Command command);

    /**
     * Retrieve a command by its name.
     * @param commandName The command name
     * @return The command which has the specified name if retrieved.
     */
    @NotNull Optional<Command> find(String commandName);

    /**
     * Remove a command from the registry.
     * @param name The name of the command to remove from the registry.
     */
    void deregister(String name);


    /**
     * Get all the discord commands data
     * @return The discord commands data
     */
    @NotNull List<CommandData> getAllDiscordCommands();
}
