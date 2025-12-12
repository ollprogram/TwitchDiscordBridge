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

class BridgeTwitchTargetTest extends CommandTest{


    private static final String FAKE_TWITCH_CHANNEL = "ollprogram";

    @BeforeEach
    void setUp() {
        command = new BridgeTwitchTarget(bridge);
    }

    @Test
    @DisplayName("Change the twitch target, with valid name")
    void testValidName(){
        when(bridge.changeTwitchChannel(FAKE_TWITCH_CHANNEL)).thenReturn(true);
        assertEquals("The target channel for twitch has been changed.", command.getExecution(List.of(FAKE_TWITCH_CHANNEL)).get());
    }

    @Test
    @DisplayName("Change the twitch target, with wrong name")
    void testWrongName(){
        when(bridge.changeTwitchChannel(FAKE_TWITCH_CHANNEL)).thenReturn(false);
        assertEquals("The channel doesn't exists or can't be retrieved.", command.getExecution(List.of(FAKE_TWITCH_CHANNEL)).get());
    }

    @Test
    @DisplayName("Too much args")
    void testTooMuchArgs(){
        assertEquals(Command.DEFAULT_ARGS_ERROR, command.getExecution(List.of("Hey", "Hey")).get());
    }

    @Test
    @DisplayName("Not enough arguments")
    void testNotEnoughArgs(){
        assertEquals(Command.DEFAULT_ARGS_ERROR, command.getExecution(List.of()).get());
    }

    @Test
    @DisplayName("Should have one option")
    void testOneOption(){
        hasValidOptionSize(1);
    }
}