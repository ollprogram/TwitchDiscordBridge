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

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BridgeInfoTest extends CommandTest{

    private BridgeConfig fakeConfig;

    private static final String FAKE_TWITCH_CHANNEL = "ollprogram";

    private static final String FAKE_DISCORD_CHANNEL = "013103013123";

    @BeforeEach
    void setUp() {
        JDA jda = mock(JDA.class);
        fakeConfig = mock(BridgeConfig.class);
        when(fakeConfig.getTwitchChannelName()).thenReturn(FAKE_TWITCH_CHANNEL);
        when(fakeConfig.getDiscordChannelID()).thenReturn(FAKE_DISCORD_CHANNEL);
        when(bridge.getConfig()).thenReturn(fakeConfig);
        command = new BridgeInfo(bridge, jda);
    }

    @Test
    @DisplayName("Bridge is opened")
    void testBridgeOpened(){
        when(bridge.isOpen()).thenReturn(true);
        assertThat(command.getExecution(List.of()).get()).matches(Pattern.compile(".*Status : (.......)?Opened.*", Pattern.DOTALL));
    }

    @Test
    @DisplayName("Bridge is closed")
    void testBridgeClosed(){
        when(bridge.isOpen()).thenReturn(false);
        assertThat(command.getExecution(List.of()).get()).matches(Pattern.compile(".*Status : (.......)?Closed.*", Pattern.DOTALL));
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