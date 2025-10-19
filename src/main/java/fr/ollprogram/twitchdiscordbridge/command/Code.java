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

public class Code implements Command{

    private static final String TEXT = """
            Hi I'm ollprogram this bot has been made with TwitchDiscordBridge.
            TwitchDiscordBridge is free software, contribute or download here : https://github.com/ollprogram/TwitchDiscordBridge
            """;

    private static final String MANUAL = """
            The Code command :
                Tells where we can find the source code.
                Example : 
                    code
            """;

    @Override
    public @NotNull String getHelp() {
        return MANUAL;
    }

    @Override
    public @NotNull Supplier<String> getExecution(String... args) {
        return () -> TEXT;
    }
}
