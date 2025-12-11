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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BridgeCloseTest extends CommandTest{

    @BeforeEach
    void setUp() {
        command = new BridgeClose(bridge);
    }

    @Test
    @DisplayName("Already closed")
    void testAlreadyClosed(){
        when(bridge.isOpen()).thenReturn(false);
        assertEquals("Already closed !", command.getExecution(List.of()).get());
    }

    @Test
    @DisplayName("Not already closed")
    void testNotAlreadyClosed(){
        when(bridge.isOpen()).thenReturn(true);
        assertEquals("The bridge is now close !", command.getExecution(List.of()).get());
    }

    @Test
    @DisplayName("Too much args")
    void testTooMuchArgs(){
        assertEquals(Command.SHOULD_HAVE_NO_ARGS_ERROR, command.getExecution(List.of("Hey")).get());
    }

    @Test
    @DisplayName("Should have no options")
    void testNoOptions(){
        hasValidOptionSize(0);
    }
}