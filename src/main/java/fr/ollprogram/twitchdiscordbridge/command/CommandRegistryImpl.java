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

import fr.ollprogram.twitchdiscordbridge.exception.AlreadyRegisteredException;
import fr.ollprogram.twitchdiscordbridge.exception.CommandNotFoundException;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of the command registry, using a HashMaps to store commands.
 * We shouldn't be able to store a command and subcommands for the same command name (this is due to discord API functioning)
 */
public class CommandRegistryImpl implements CommandRegistry {

    /**
     * An inner class which is used to store a single command or multiple subcommands.
     * We shouldn't be able to store a command and subcommands for the same command name (this is due to discord API functioning)
     */
    private static class Entry {
        private Map<String, Command> subcommands;

        private Command command;

        private DefaultMemberPermissions permissions;
        private boolean anyEnabled;

        /**
         * Constructor for single command entry
         * @param command The command
         */
        Entry(Command command){
            this();
            this.command = command;
            this.subcommands = null;
            anyEnabled = command.isDiscordEnabled();
        }

        /**
         * Constructor of an Empty entry, might have some subcommands
         */
        Entry(){
            anyEnabled = false;
            this.permissions = DefaultMemberPermissions.ENABLED;
        }

        /**
         * Add a subcommand to the entry
         * @param name The name of the subcommand
         * @param subcommand The subcommand
         */
        void addSubcommand(String name, Command subcommand){
            if(subcommands == null){
                this.subcommands = new HashMap<>();
            }
            Command command = subcommands.get(name);
            if(command != null) throw new AlreadyRegisteredException("The subcommand "+name+"was already registered");
            subcommands.put(name, subcommand);
            anyEnabled = anyEnabled || subcommand.isDiscordEnabled();
        }

        /**
         * Get the command
         * @return The command
         */
        Command getCommand(){
            return command;
        }

        /**
         * Check if it's a subcommand entry
         * @return if it's a subcommand entry
         */
        boolean hasSubcommands(){
            return command == null && this.subcommands != null && !this.subcommands.isEmpty();
        }

        /**
         * Check if it's a single command entry
         * @return if it's a single command entry
         */
        boolean isCommand(){
            return !hasSubcommands();
        }

        /**
         * Get the subcommand by its name
         * @param commandName The subcommand name
         * @return The subcommand
         */
        Command getSubcommand(String commandName){
            if(this.hasSubcommands()){
                return subcommands.get(commandName);
            }
            return null;
        }

        /**
         * Set the permission for the entire entry (single command or all subcommands)
         * @param permissions The member permissions
         */
        void setPermissions(DefaultMemberPermissions permissions){
            this.permissions = permissions;
        }

        /**
         * Get the discord equivalent command data if any of the command / subcommand is enabled for discord usage
         * @param name The name of the root command / single command
         * @return The Discord equivalent command data
         */
        SlashCommandData getDiscordCommand(String name){ //might be too complex?
            SlashCommandData data = Commands.slash(name, "Command having subcommands");
            if(anyEnabled) data.setDefaultPermissions(permissions);
            else return null;
            if(isCommand()){
                data.setDescription(command.getDescription());
                data.setDefaultPermissions(permissions);
                command.getOptions().parallelStream().forEach(option -> data.addOption(OptionType.STRING, option.name(), option.description(), option.mandatory()));
            } else if(hasSubcommands()) {
                subcommands.forEach((subName, sub) -> {
                    if(sub.isDiscordEnabled()){
                        SubcommandData subData = new SubcommandData(subName, sub.getDescription());
                        sub.getOptions().parallelStream().forEach(option -> subData.addOption(OptionType.STRING, option.name(), option.description(), option.mandatory()));
                        data.addSubcommands(subData);
                    }
                });
            }
            return data;
        }
    }


    private final Map<String, Entry> commands;

    /**
     * Constructor
     */
    public CommandRegistryImpl(){
        commands = new HashMap<>();
    }


    @Override
    public void register(@NotNull String commandName, @NotNull Command command) {
        Entry entry = commands.get(commandName);
        if(entry == null) {
            commands.put(commandName, new Entry(command));
        }else throw new AlreadyRegisteredException("Command "+commandName+" is already registered");
    }

    @Override
    public void register(@NotNull String commandName, @NotNull String subcommandName, @NotNull Command subcommand) {
        Entry entry = commands.get(commandName);
        if(entry == null){
            Entry newEntry = new Entry();
            commands.put(commandName, newEntry);
            newEntry.addSubcommand(subcommandName, subcommand);
        } else {
            if(entry.isCommand()) throw new AlreadyRegisteredException("The root command was already registered as q simple command");
            entry.addSubcommand(subcommandName, subcommand);
        }
    }

    @Override
    public @NotNull Optional<Command> getSubcommand(@NotNull String commandName, @NotNull String subcommandName) {
        Entry rootCommand = commands.get(commandName);
        if(rootCommand == null) return Optional.empty();
        Command subcommand = rootCommand.getSubcommand(subcommandName);
        return Optional.ofNullable(subcommand);
    }

    @Override
    public @NotNull Optional<Command> getCommand(@NotNull String commandName) {
        Entry rootCommand = commands.get(commandName);
        if(rootCommand == null) return Optional.empty();
        return Optional.ofNullable(rootCommand.getCommand());
    }

    @Override
    public @NotNull List<@NotNull CommandData> getAllDiscordCommands() {
        List<CommandData> commandDataList = new ArrayList<>();
        commands.forEach((name, entry) -> {
            CommandData data = entry.getDiscordCommand(name);
            if(data != null) commandDataList.add(data);
        });
        return commandDataList;
    }

    @Override
    public @NotNull String getHelp() {
        StringBuilder builder = new StringBuilder("All commands :\n\n");
        commands.forEach((name, entry) -> {
            if(entry.isCommand()) {
                Command command = entry.command;
                builder.append(command.getHelp(name));
            }
            else if(entry.hasSubcommands()){
                entry.subcommands.forEach((subName, sub) -> builder.append(sub.getHelp(name+" "+subName)));
            }
        });
        return builder.toString();
    }

    @Override
    public void setDiscordPermissions(@NotNull String commandName, @NotNull DefaultMemberPermissions permissions) {
        Entry entry = commands.get(commandName);
        if(entry == null) throw new CommandNotFoundException("The given command should exists.");
        entry.setPermissions(permissions);
    }
}
