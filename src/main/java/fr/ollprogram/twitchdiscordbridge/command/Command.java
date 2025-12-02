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

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * The abstract class of a command.
 * Commands can be executed on the CLI or on discord if specified.
 * For now, it's not possible to create a command that should be executed only on discord
 */
public abstract class Command {
    protected static final String DEFAULT_ARGS_ERROR = "Wrong arguments";

    protected static final String SHOULD_HAVE_NO_ARGS_ERROR = "This command don't have any arguments.";
    private final String description;

    private final boolean discordEnabled;

    private final List<Option> options;

    private int argsMin;
    private int argsMax;

    /**
     * Main constructor
     * @param description The command description
     * @param discordEnabled If this command can be used on discord,
     * @param options The command arguments
     */
    protected Command(@NotNull String description, @NotNull List<@NotNull Option> options, boolean discordEnabled){
        this.description = description;
        this.discordEnabled = discordEnabled;
        this.options = Collections.unmodifiableList(options);
        argsMin = 0;
        argsMax = 0;
        this.options.parallelStream().forEach((a) -> {
            argsMax++;
            if(a.mandatory()) argsMin ++;
        });
    }

    /**
     * without options constructor
     * @param description The command description
     * @param discordEnabled If this command can be used on discord,
     */
    protected Command(@NotNull String description, boolean discordEnabled){
        this(description, List.of(), discordEnabled);
    }

    /**
     * Get the code to execute as a supplier which should return a string as a bot reply
     * @param args The arguments of the command needed for the execution
     * @return The supplier representing the code to execute
     */
    public abstract @NotNull Supplier<@NotNull String> getExecution(@NotNull List<@NotNull String> args);

    /**
     * Validate the arguments
     * @param args The arguments
     * @return If arguments are valid
     */
    protected boolean validateArguments(@NotNull List<@NotNull String> args) {
        int argsNumber = args.size();
        return argsNumber >= argsMin && argsNumber <= argsMax;
    }

    /**
     * Get the command description
     * @return The command description.
     */
    public @NotNull String getDescription(){
        return this.description;
    }

    /**
     * If the command can be used on discord
     * @return If the command can be used on discord
     */
    public boolean isDiscordEnabled() {
        return discordEnabled;
    }

    /**
     * Get options
     * @return The options list
     */
    public @NotNull List<@NotNull Option> getOptions(){
        return options;
    }

    /**
     * The help string of the command
     * @param commandName The full command name (including subcommand name if it's the case)
     * @return The help string of the command
     */
    public @NotNull String getHelp(String commandName){
        StringBuilder builder = new StringBuilder();
        builder.append("- ")
                .append(commandName)
                .append(" : ").append(this.getDescription()).append(argsMax > 0 ? "\n\tArguments : \n" : "\n");
        this.getOptions().parallelStream().forEach((a) -> builder.append("\t\t")
                .append(a.name())
                .append(" : ")
                .append(a.description()).append(a.mandatory() ? " [Mandatory]" : "")
                .append("\n"));
        return builder.toString();
    }
}
