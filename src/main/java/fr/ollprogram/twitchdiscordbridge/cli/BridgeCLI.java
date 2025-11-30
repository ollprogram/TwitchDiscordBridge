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

package fr.ollprogram.twitchdiscordbridge.cli;

import fr.ollprogram.twitchdiscordbridge.command.Command;
import fr.ollprogram.twitchdiscordbridge.command.CommandRegistry;
import fr.ollprogram.twitchdiscordbridge.command.TDBExecutor;
import fr.ollprogram.twitchdiscordbridge.manager.AppsManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Command interface for the user. This command interface has some exclusive commands not runnable from discord
 */
public class BridgeCLI {
    private final Scanner scanner;

    private final CommandRegistry registry;

    private final AppsManager appsManager;

    private final static Logger LOG = LoggerFactory.getLogger("Bridge CLI");

    /**
     * Constructor
     * @param scanner The input stream scanner
     * @param registry The command registry
     * @param appsManager The applications manager
     */
    public BridgeCLI(@NotNull Scanner scanner, @NotNull CommandRegistry registry, @NotNull AppsManager appsManager){
        this.scanner = scanner;
        this.appsManager = appsManager;
        this.registry = registry;
    }

    /**
     * Run the CLI, this will block the current thread until the application shutdown
     */
    public void run(){
        while(appsManager.areAllRunning()){
            String fullCommandLine = askCommand();
            boolean isPrimary = handlePrimaryCommands(fullCommandLine);
            if(!isPrimary) handleRegistryCommands(fullCommandLine);
        }
        System.out.println("See you next time !");
    }

    /**
     * handle the native commands
     * @param fullCommandLine the full command line
     * @return if command matches one of the primary commands
     */
    private boolean handlePrimaryCommands(String fullCommandLine){
        if(fullCommandLine.equalsIgnoreCase("help")){
            System.out.println(registry.getHelp());
            return true;
        }
        if(fullCommandLine.equalsIgnoreCase("shutdown")){ //can't be executed by executor with a command (causing deadlocks)
            try {
                appsManager.shutdownAll();
            } catch (InterruptedException e) {
                LOG.error("Shutdown task have been interrupted");
                System.exit(1);
            }
            return true;
        }
        if(fullCommandLine.equalsIgnoreCase("shutdown now")){ //can't be executed by executor with a command (causing deadlocks)
            try {
                appsManager.shutdownAllNow();
            } catch (InterruptedException e) {
                LOG.error("Shutdown task have been interrupted");
                System.exit(1);
            }
            return true;
        }
        return false;
    }

    /**
     * Handle the commands form the registry
     * @param fullCommandLine The full command line
     */
    private void handleRegistryCommands(String fullCommandLine){
        TDBExecutor executor = appsManager.getExecutor();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        List<String> args = pattern.matcher(fullCommandLine)
                .results()
                .map(m -> m.group(1) != null ? m.group(1) : m.group(2))
                .toList();
        int argsSize = args.size();
        if(argsSize > 0){
            Optional<Command> commandOpt = registry.getCommand(args.get(0));
            Optional<Command> subCommandOpt = Optional.empty();
            if(argsSize > 1) subCommandOpt = registry.getSubcommand(args.get(0), args.get(1));
            if(commandOpt.isEmpty() && subCommandOpt.isEmpty()){
                System.out.println("Command not found, please try 'help' to see all commands.");
            }else {
                try {
                    String res;
                    if(subCommandOpt.isPresent()) {
                        res = executor.submit(subCommandOpt.get(), args.subList(2, argsSize)).get(); //sequential (joining the thread for better user experience)
                    } else {
                        res = executor.submit(commandOpt.get(), args.subList(1, argsSize)).get(); //sequential (joining the thread for better user experience)
                    }
                    System.out.println(res);
                } catch (InterruptedException | ExecutionException e) {
                    LOG.warn("The following error occurs during the command execution "+e.getMessage());
                    System.out.println("Command failed");
                }
            }
        }
    }


    /**
     * Ask a command on the terminal
     * @return The command full string written by the user.
     */
    private String askCommand(){
        System.out.println("Ready to execute a command (type help to see all commands) : ");
        return scanner.nextLine();
    }


}

