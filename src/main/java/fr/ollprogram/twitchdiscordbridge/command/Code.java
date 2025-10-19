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
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class Code implements Command{

    private static final String TEXT = """
            Hi I'm ollprogram this bot has been made with TwitchDiscordBridge.
            TwitchDiscordBridge is free software, contribute or download here : https://github.com/ollprogram/TwitchDiscordBridge
            """;

    private static final String DESCRIPTION = """
            Information about the code of this bot
            """;

    @Override
    public @NotNull Supplier<String> getExecution(String... args) {
        return () -> TEXT;
    }

    @Override
    public @NotNull String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public @NotNull Optional<CommandData> asDiscordCommand(String name){
        return Optional.of(Commands.slash(name, getDescription()));
    }

}
