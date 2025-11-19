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
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the command registry, using a HashMap to store commands.
 */
public class CommandRegistryImpl implements CommandRegistry {

    private static class Entry {
        private Map<String, Command> subcommands;

        private Command command;

        Entry(Command command){
            this.command = command;
            this.subcommands = null;
        }

        Entry(){
            this.command = null;
            this.subcommands = null;
        }

        void addSubcommand(String name, Command subCommand){
            if(subcommands == null){
                this.subcommands = new HashMap<>();
            }
            Command command = subcommands.get(name);
            if(command != null) throw new AlreadyRegisteredException("The subcommand "+name+"was already registered");
            subcommands.put(name, subCommand);
        }

        void setCommandType(Command command) {
            this.command = command;
        }

        Command getCommandType(){
            return command;
        }

        boolean hasSubcommands(){
            return this.subcommands != null && !this.subcommands.isEmpty();
        }

        boolean isCommand(){
            return command != null;
        }

        Command getSubcommand(String commandName){
            if(this.hasSubcommands()){
                return subcommands.get(commandName);
            }
            return null;
        }

        void removeSubcommand(String commandName){
            if(this.hasSubcommands()){
                subcommands.remove(commandName);
                if(subcommands.isEmpty()) subcommands = null; //less memory cost
            }
        }
    }


    private final Map<String, Entry> commands;

    public CommandRegistryImpl(){
        commands = new HashMap<>();
    }


    @Override
    public void register(@NotNull String commandName, @NotNull Command command) {
        Entry entry = commands.get(commandName);
        if(entry == null) {
            commands.put(commandName, new Entry(command));
        }else {
            Command type = entry.getCommandType();
            if(type != null) throw new AlreadyRegisteredException("Command "+commandName+" is already registered");
            entry.setCommandType(command);
        }
    }

    @Override
    public void register(@NotNull String commandName, @NotNull String subcommandName, @NotNull Command subcommand) {
        Entry entry = commands.get(commandName);
        if(entry == null){
            Entry newEntry = new Entry();
            commands.put(commandName, newEntry);
            newEntry.addSubcommand(subcommandName, subcommand);
        } else {
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
        return Optional.ofNullable(rootCommand.command);
    }

    @Override
    public void deregister(@NotNull String commandName) {
        commands.remove(commandName);
    }

    @Override
    public void deregister(@NotNull String commandName, @NotNull String subcommandName) {
        Entry rootCommand = commands.get(commandName);
        if(rootCommand == null) return;
        rootCommand.removeSubcommand(subcommandName);
    }

    @Override
    public @NotNull List<CommandData> getAllDiscordCommands() {
        return List.of(); //TODO
    }

    @Override
    public @NotNull String getHelp() {
        StringBuilder builder = new StringBuilder("All commands :\n");
        commands.forEach((name, entry) -> {
            if(entry.isCommand()) builder.append("- ")
                    .append(name)
                    .append(" : ")
                    .append(entry.command.getDescription()).append("\n");
            if(entry.hasSubcommands()){
                entry.subcommands.forEach((subName, sub) -> {
                    builder.append("- ")
                            .append(name).append(" ").append(subName)
                            .append(" : ")
                            .append(sub.getDescription()).append("\n");
                });
            }
        });
        return builder.toString();
    }
}
