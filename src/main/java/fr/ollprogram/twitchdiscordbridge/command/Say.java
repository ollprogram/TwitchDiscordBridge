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

import fr.ollprogram.twitchdiscordbridge.bridge.Bridge;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Say implements Command {

    private static final String TEXT = "Message sent to both applications.";

    private static final String DESCRIPTION = """
            Send a message on both application (twitch and discord)
            """;

    private static final String  MESSAGE_PREFIX = "Admin says : ";
    private static final String  ARGS_ERROR = "No message given";

    private Bridge bridge;

    public Say(Bridge bridge){
        this.bridge = bridge;
    }

    @Override
    public @NotNull Supplier<String> getExecution(List<String> args) {
        if(args.size() < 1){
            return () -> ARGS_ERROR;
        }
        return () -> {
            String message = MESSAGE_PREFIX + args.parallelStream().reduce("", String::concat);
            bridge.sendToDiscord(message);
            bridge.sendToTwitch(message);
            return TEXT;
        };
    }

    @Override
    public @NotNull String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public @NotNull Optional<CommandData> asDiscordCommand(String name) {
        return Optional.of(Commands.slash(name, DESCRIPTION)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.STRING, "message", "The message to send", true)
        );
    }
}
