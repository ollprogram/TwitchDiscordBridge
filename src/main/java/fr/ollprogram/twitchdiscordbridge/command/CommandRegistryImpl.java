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

import java.util.*;

/**
 * Implementation of the command registry, using a HashMap to store commands.
 */
public class CommandRegistryImpl implements CommandRegistry {

    private final Map<String, Command> commandsMap;

    public CommandRegistryImpl(){
        commandsMap = new HashMap<>();
    }

    @Override
    public void register(String name, Command command) {
        commandsMap.put(name, command);
    }

    @Override
    public @NotNull Optional<Command> find(String commandName) {
        return Optional.ofNullable(commandsMap.get(commandName));
    }

    @Override
    public void deregister(String name) {
        commandsMap.remove(name);
    }

    @Override
    public @NotNull List<CommandData> getAllDiscordCommands() {
        List<CommandData> discordCommands = new ArrayList<>();
        commandsMap.forEach((name,command) -> {
            Optional<CommandData> optionalCommandData = command.asDiscordCommand(name);
            optionalCommandData.ifPresent(discordCommands::add);
        });
        return discordCommands;
    }

    @Override
    public @NotNull String getHelp() {
        StringBuilder builder = new StringBuilder();
        commandsMap.forEach((name,command) -> {builder.append("- ").append(name).append(" : ").append(command.getDescription()).append("\n");});
        return builder.toString();
    }
}
