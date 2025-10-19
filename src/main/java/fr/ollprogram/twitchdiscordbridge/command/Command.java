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
import java.util.function.Supplier;

public interface Command {

    /**
     * Get the code to execute as a supplier which should return a string as a bot reply
     * @param args The arguments of the command needed for the execution
     * @return The supplier representing the code to execute
     */
    @NotNull Supplier<String> getExecution(List<String> args);

    /**
     * Get the command description
     * @return The command description.
     */
    @NotNull String getDescription();

    /**
     * Get the discord command if compatible
     * @param name The command name
     * @return the discord command if compatible
     */
    default @NotNull Optional<CommandData> asDiscordCommand(String name){
        return Optional.empty();
    }

}
