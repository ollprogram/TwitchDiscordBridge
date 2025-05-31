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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandRegistryTest {
    private Command fakeCommand1;
    private Command fakeCommand2;

    private CommandRegistry commandRegistry;

    @BeforeEach
    void beforeRegistryTests(){
        fakeCommand1 = mock(Command.class);
        when(fakeCommand1.getName()).thenReturn("test_command1");
        fakeCommand2 = mock(Command.class);
        when(fakeCommand2.getName()).thenReturn("test_command2");
        commandRegistry = new CommandRegistryImpl();
    }

    @Test
    void testRegisterOneCommand(){
        commandRegistry.register(fakeCommand1);
        Command retrievedCommand = commandRegistry.find("test_command1").orElse(null);
        assertEquals(fakeCommand1, retrievedCommand);
    }

    @Test
    void testRegisterTwoCommands1(){
        commandRegistry.register(fakeCommand1);
        commandRegistry.register(fakeCommand2);
        Command retrievedCommand = commandRegistry.find("test_command1").orElse(null);
        assertEquals(fakeCommand1, retrievedCommand);
    }

    @Test
    void testRegisterTwoCommands2(){
        commandRegistry.register(fakeCommand1);
        commandRegistry.register(fakeCommand2);
        Command retrievedCommand = commandRegistry.find("test_command2").orElse(null);
        assertEquals(fakeCommand2, retrievedCommand);
    }

    @Test
    void testRegisterTwoCommands3(){
        commandRegistry.register(fakeCommand1);
        commandRegistry.register(fakeCommand2);
        Optional<Command> retrievedCommand = commandRegistry.find("test_command3");
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    void testDeregister(){
        commandRegistry.register(fakeCommand1);
        commandRegistry.deregister(fakeCommand1);
        Optional<Command> retrievedCommand = commandRegistry.find("test_command1");
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    void testDeregisterNoSideEffect(){
        commandRegistry.register(fakeCommand1);
        commandRegistry.register(fakeCommand2);
        commandRegistry.deregister(fakeCommand1);
        Optional<Command> retrievedCommand = commandRegistry.find("test_command2");
        assertEquals(fakeCommand2, retrievedCommand.orElse(null));
        retrievedCommand = commandRegistry.find("test_command1");
        assertEquals(Optional.empty(), retrievedCommand);
    }

    @Test
    void testNothingRegistered(){
        Optional<Command> retrievedCommand = commandRegistry.find("test_command1");
        assertEquals(Optional.empty(), retrievedCommand);
    }


}


