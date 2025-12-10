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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class CommandRegistryTest {
    private Command fakeCommand1;

    private static final String COMMAND_NAME1 = "test_command1";
    private static final String COMMAND_NAME2 = "test_command2";

    private static final String SUB_COMMAND_NAME1 = "test_subcommand1";

    private static final String SUB_COMMAND_NAME2 = "test_subcommand2";
    private Command fakeCommand2;

    private Command fakeSubcommand1;

    private Command fakeSubcommand2;

    private CommandRegistry commandRegistry;

    @BeforeEach
    void beforeRegistryTests(){
        fakeCommand1 = mock(Command.class);
        fakeCommand2 = mock(Command.class);
        fakeSubcommand1 = mock(Command.class);
        fakeSubcommand2 = mock(Command.class);
        commandRegistry = new CommandRegistryImpl();
    }

    @Test
    @DisplayName("Register and retrieve one command")
    void testRegisterOneCommand(){
        commandRegistry.register(COMMAND_NAME1, fakeCommand1);
        Command retrievedCommand = commandRegistry.getCommand(COMMAND_NAME1).orElse(null);
        assertEquals(fakeCommand1, retrievedCommand);
    }

    @Test
    @DisplayName("Register 2 commands and retrieve command 1")
    void testRegisterTwoCommands1(){
        commandRegistry.register(COMMAND_NAME1, fakeCommand1);
        commandRegistry.register(COMMAND_NAME2, fakeCommand2);
        Command retrievedCommand = commandRegistry.getCommand(COMMAND_NAME1).orElse(null);
        assertEquals(fakeCommand1, retrievedCommand);
    }

    @Test
    @DisplayName("Register 2 commands and retrieve command 2")
    void testRegisterTwoCommands2(){
        commandRegistry.register(COMMAND_NAME1, fakeCommand1);
        commandRegistry.register(COMMAND_NAME2, fakeCommand2);
        Command retrievedCommand = commandRegistry.getCommand(COMMAND_NAME2).orElse(null);
        assertEquals(fakeCommand2, retrievedCommand);
    }

    @Test
    @DisplayName("Register 2 commands and not retrieve command 3")
    void testRegisterTwoCommands3(){
        commandRegistry.register(COMMAND_NAME1, fakeCommand1);
        commandRegistry.register(COMMAND_NAME2, fakeCommand2);
        Optional<Command> retrievedCommand = commandRegistry.getCommand("test_command3");
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    @DisplayName("Empty registry")
    void testNothingRegistered(){
        Optional<Command> retrievedCommand = commandRegistry.getCommand(COMMAND_NAME1);
        assertEquals(Optional.empty(), retrievedCommand);
    }


    @Test
    @DisplayName("Registered subcommand can be retrieved")
    void testRegisterSubcommand(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        Command retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME1, SUB_COMMAND_NAME1).orElse(null);
        assertEquals(fakeSubcommand1, retrievedCommand);
    }

    @Test
    @DisplayName("Not registered subcommand")
    void testNotRegisterSubcommand(){
        Optional<Command> retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME1, SUB_COMMAND_NAME1);
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    @DisplayName("Command not registered but subcommand is registered")
    void testRegisterSubcommandNotCommand(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        Optional<Command> retrievedCommand = commandRegistry.getCommand(COMMAND_NAME1);
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Can't register subcommand and command")
    void testRegisterSubcommandAndCommand(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        assertThrows(AlreadyRegisteredException.class, () -> {commandRegistry.register(COMMAND_NAME1, fakeCommand1);});
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Can't register subcommand and command, commutation")
    void testRegisterSubcommandAndCommandCommutation(){
        commandRegistry.register(COMMAND_NAME1, fakeCommand1);
        assertThrows(AlreadyRegisteredException.class, () -> {commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);});
    }

    @Test
    @DisplayName("Two subcommands")
    void testRegisterTwoSubcommands1(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME2, fakeSubcommand2);
        Command retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME1, SUB_COMMAND_NAME1).orElse(null);
        assertEquals(fakeSubcommand1, retrievedCommand);
    }

    @Test
    @DisplayName("Two subcommands")
    void testRegisterTwoSubcommands2(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME2, fakeSubcommand2);
        Command retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME1, SUB_COMMAND_NAME2).orElse(null);
        assertEquals(fakeSubcommand2, retrievedCommand);
    }

    @Test
    @DisplayName("Two subcommands, different groups")
    void testRegisterTwoSubcommands3(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        commandRegistry.register(COMMAND_NAME2, SUB_COMMAND_NAME2, fakeSubcommand2);
        Command retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME1, SUB_COMMAND_NAME1).orElse(null);
        assertEquals(fakeSubcommand1, retrievedCommand);
    }

    @Test
    @DisplayName("Two subcommands, different groups")
    void testRegisterTwoSubcommands4(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        commandRegistry.register(COMMAND_NAME2, SUB_COMMAND_NAME2, fakeSubcommand2);
        Command retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME2, SUB_COMMAND_NAME2).orElse(null);
        assertEquals(fakeSubcommand2, retrievedCommand);
    }

    @Test
    @DisplayName("Two subcommands, different groups, not registered")
    void testNotRegisteredTwoSubcommands1(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        commandRegistry.register(COMMAND_NAME2, SUB_COMMAND_NAME2, fakeSubcommand2);
        Optional<Command> retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME1, SUB_COMMAND_NAME2);
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    @DisplayName("Two subcommands, different groups, not registered")
    void testNotRegisteredTwoSubcommands2(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        commandRegistry.register(COMMAND_NAME2, SUB_COMMAND_NAME2, fakeSubcommand2);
        Optional<Command> retrievedCommand = commandRegistry.getSubcommand(COMMAND_NAME2, SUB_COMMAND_NAME1);
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Already registered command")
    void testAlreadyRegisteredCommand(){
        commandRegistry.register(COMMAND_NAME1, fakeCommand1);
        assertThrows(AlreadyRegisteredException.class, () -> {
            commandRegistry.register(COMMAND_NAME1, fakeCommand2);
        });
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Already registered subcommand")
    void testAlreadyRegisteredSubcommand(){
        commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand1);
        assertThrows(AlreadyRegisteredException.class, () -> {
            commandRegistry.register(COMMAND_NAME1, SUB_COMMAND_NAME1, fakeSubcommand2);
        });
    }

}


