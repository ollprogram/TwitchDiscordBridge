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

import java.util.function.Supplier;

public interface Command {
    /**
     * Get the command help
     * @return The manual of the command
     */
    @NotNull String getHelp();

    /**
     * Get the code to execute as a supplier which should return a string as a bot reply
     * @param args The arguments of the command needed for the execution
     * @return The supplier representing the code to execute
     */
    @NotNull Supplier<String> getExecution(String... args);

    //TODO add a method or something to be able to register all slash commands to discord in a way as generic as possible

}
