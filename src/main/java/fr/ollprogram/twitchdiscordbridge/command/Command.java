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

import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class Command {

    private final String description;
    private final int argsMin;

    private final int argsMax;

    private Permission discordPermission;

    private final boolean discordEnabled;

    /**
     * Main constructor
     * @param description The command description
     * @param argsMin The minimum number of arguments
     * @param argsMax The maximum number of arguments -1 if no limit
     * @param discordEnabled If this command can be used on discord,
     */
    protected Command(@NotNull String description, int argsMin, int argsMax, boolean discordEnabled){
        this.description = description;
        this.argsMin = argsMin;
        this.argsMax = argsMax;
        this.discordEnabled = discordEnabled;
        this.discordPermission = null;
    }

    /**
     * Discord command constructor with permissions
     * @param description The description
     * @param argsMin The minimum number of arguments
     * @param argsMax The maximum number of arguments -1 if no limit
     * @param discordPermission The discord permission for this command
     */
    protected Command(@NotNull String description, int argsMin, int argsMax, @NotNull Permission discordPermission){
        this(description, argsMin, argsMax, true);
        this.discordPermission = discordPermission;
    }

    /**
     * Get the code to execute as a supplier which should return a string as a bot reply
     * @param args The arguments of the command needed for the execution
     * @return The supplier representing the code to execute
     */
    public abstract @NotNull Supplier<String> getExecution(@NotNull List<String> args);

    /**
     * Validate the arguments
     * @param args The arguments
     * @return If arguments are valid
     */
    protected boolean validateArguments(@NotNull List<String> args) {
        int argsNumber = args.size();
        return argsNumber >= argsMin && (argsMax == -1 || argsNumber <= argsMax);
    }

    /**
     * Get the command description
     * @return The command description.
     */
    public @NotNull String getDescription(){
        return this.description;
    }

    /**
     * If the command has discord permission get the permission type
     * @return The discord Permission of the command
     */
    public Optional<Permission> getDiscordPermission() {
        return Optional.ofNullable(discordPermission);
    }

    /**
     * If the command can be used on discord
     * @return If the command can be used on discord
     */
    public boolean isDiscordEnabled() {
        return discordEnabled;
    }
}
