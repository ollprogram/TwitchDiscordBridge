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
     * Register a command to the registry. A command can be registered only once.
     * @param commandName The command name
     * @param command The command to register.
     */
    void register(@NotNull String commandName, @NotNull Command command);

    /**
     * Register a subcommand. A subcommand can be registered only once.
     * @param commandName The root command name
     * @param subcommandName The subcommand name
     * @param subcommand The subcommand type
     */
    void register(@NotNull String commandName, @NotNull String subcommandName, @NotNull Command subcommand);

    /**
     * Retrieve a subcommand by the command name and group name
     * @param commandName The root command name
     * @param subcommandName The subcommand name
     * @return The Command
     */
    @NotNull Optional<Command> getSubcommand(@NotNull String commandName, @NotNull String subcommandName);

    /**
     * Retrieve a command by the command name and group name
     * @param commandName The root command name
     * @return The Command to be executed
     */
    @NotNull Optional<Command> getCommand(@NotNull String commandName);

    /**
     * Remove a command from the registry. No effects if already deregistered.
     * @param commandName The name of the command to remove from the registry.
     */
    void deregister(@NotNull String commandName);

    /**
     * Remove a subcommand from the registry. No effects if already deregistered.
     * @param commandName The root command name
     * @param subcommandName The name of the command to remove from the registry.
     */
    void deregister(@NotNull String commandName, @NotNull String subcommandName);

    /**
     * Get all the discord commands data
     * @return The discord commands data
     */
    @NotNull List<CommandData> getAllDiscordCommands();

    /**
     * Get all commands help
     * @return Help string
     */
    @NotNull String getHelp();
}
