/* Copyright © 2024 ollprogram
 *
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

import java.io.PrintStream;

public class Code implements Command{

    private static final String text = "Hi I'm ollprogram this bot has been made with TwitchDiscordBridge.\n" +
            "TwitchDiscordBridge is free software, contribute or download here : https://github.com/ollprogram/TwitchDiscordBridge";
    private PrintStream out;

    public Code(@NotNull PrintStream out){
        this.out = out;
    }

    @Override
    public String getName() {
        return "code";
    }

    @Override
    public void execute() {
        out.println(text);
    }
}
