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
 * Implementation of the command registry, using a HashMap to store commands.
 */
public class CommandRegistryImpl implements CommandRegistry {

    private static class Entry {
        private Map<String, Command> subcommands;

        private Command command;

        private DefaultMemberPermissions permissions;
        private boolean anyEnabled;

        Entry(Command command){
            this();
            this.command = command;
            this.subcommands = null;
            anyEnabled = command.isDiscordEnabled();
        }

        Entry(){
            anyEnabled = false;
            this.permissions = DefaultMemberPermissions.ENABLED;
        }

        void addSubcommand(String name, Command subommand){
            if(subcommands == null){
                this.subcommands = new HashMap<>();
            }
            Command command = subcommands.get(name);
            if(command != null) throw new AlreadyRegisteredException("The subcommand "+name+"was already registered");
            subcommands.put(name, subommand);
            anyEnabled = anyEnabled || subommand.isDiscordEnabled();
        }

        Command getCommand(){
            return command;
        }

        boolean hasSubcommands(){
            return this.subcommands != null && !this.subcommands.isEmpty();
        }

        boolean isCommand(){
            return command != null && !hasSubcommands();
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

        void setPermissions(DefaultMemberPermissions permissions){
            this.permissions = permissions;
        }

        SlashCommandData getDiscordCommand(String name){ //might be too complex?
            SlashCommandData data = Commands.slash(name, "Command having subcommands");
            if(anyEnabled) data.setDefaultPermissions(permissions);
            else return null;
            if(isCommand()){
                data.setDescription(command.getDescription());
                data.setDefaultPermissions(permissions);
                command.getOptions().parallelStream().forEach(option -> {
                    data.addOption(OptionType.STRING, option.name(), option.description(), option.mandatory());
                });
            } else if(hasSubcommands()) {
                subcommands.forEach((subName, sub) -> {
                    if(sub.isDiscordEnabled()){
                        SubcommandData subData = new SubcommandData(subName, sub.getDescription());
                        sub.getOptions().parallelStream().forEach(option -> {
                            data.addOption(OptionType.STRING, option.name(), option.description(), option.mandatory());
                        });
                        data.addSubcommands(subData);
                    }
                });
            }
            return data;
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
    public @NotNull List<CommandData> getAllDiscordCommands() {
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
                entry.subcommands.forEach((subName, sub) -> {
                    builder.append(sub.getHelp(name+" "+subName));
                });
            }
        });
        return builder.toString();
    }

    @Override
    public void setDiscordPermissions(@NotNull String commandName, DefaultMemberPermissions permissions) {
        Entry entry = commands.get(commandName);
        if(entry == null) throw new CommandNotFoundException("The given command should exists.");
        entry.setPermissions(permissions);
    }
}
