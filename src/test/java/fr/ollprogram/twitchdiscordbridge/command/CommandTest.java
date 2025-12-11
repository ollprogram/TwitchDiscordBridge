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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public abstract class CommandTest {

    protected Bridge bridge;
    protected Command command;

    private static final String FAKE_NAME = "CommandName";

    @BeforeEach
    void superSetUp(){
        bridge = mock(Bridge.class);
    }

    @Test
    @DisplayName("Not empty description")
    void testNotEmptyDescription(){
        assertFalse(command.getDescription().isEmpty());
    }

    @Test
    @DisplayName("Help contains name and description")
    void testHelp(){
        String help = command.getHelp(FAKE_NAME);
        assertTrue(help.contains(FAKE_NAME) && help.contains(command.getDescription()));
    }

    protected void hasValidOptionSize(int expected){
        assertEquals(expected, command.getOptions().size());
    }

}
