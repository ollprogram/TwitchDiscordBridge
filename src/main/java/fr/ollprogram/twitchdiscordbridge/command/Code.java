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
import java.util.function.Supplier;


public class Code extends Command {

    private static final String TEXT = """
            Hi I'm ollprogram this bot has been made with TwitchDiscordBridge.
            TwitchDiscordBridge is free software, contribute or download here : https://github.com/ollprogram/TwitchDiscordBridge
            """;

    private static final String DESCRIPTION = "Information about the code of this bot";



    public Code() {
        super(DESCRIPTION,true);
    }

    @Override
    public @NotNull Supplier<@NotNull String> getExecution(@NotNull List<@NotNull String> args) {
        if(super.validateArguments(args)){
            return () -> TEXT;
        } else {
            return () -> SHOULD_HAVE_NO_ARGS_ERROR;
        }

    }

}
