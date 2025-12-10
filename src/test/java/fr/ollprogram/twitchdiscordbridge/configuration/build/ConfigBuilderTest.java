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

package fr.ollprogram.twitchdiscordbridge.configuration.build;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.exception.IncompleteConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigBuilderTest {

    private ConfigBuilder b;
    @BeforeEach
    void before(){
        b = new ConfigBuilderImpl();
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete empty")
    void incompleteEmpty(){
        assertThrows(IncompleteConfigurationException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing discord token")
    void incompleteOne1(){
        b.setDiscordChannelID("").setTwitchToken("").setTwitchChannelName("");
        assertThrows(IncompleteConfigurationException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing twitch token")
    void incompleteOne2(){
        b.setDiscordChannelID("").setTwitchChannelName("").setDiscordToken("");
        assertThrows(IncompleteConfigurationException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing discord channel")
    void incompleteOne3(){
        b.setTwitchToken("").setTwitchChannelName("").setDiscordToken("");
        assertThrows(IncompleteConfigurationException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing twitch channel")
    void incompleteOne4(){
        b.setDiscordChannelID("").setTwitchToken("").setDiscordToken("");
        assertThrows(IncompleteConfigurationException.class, () -> b.build());
    }

    @Test
    @DisplayName("complete")
    void complete(){
        b.setDiscordChannelID("").setTwitchToken("").setDiscordToken("").setTwitchChannelName("");
        assertTrue(b.isComplete());
    }

    @Test
    @DisplayName("incomplete empty")
    void incomplete(){
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing twitch channel")
    void incompleteOneTC(){
        b.setDiscordChannelID("").setTwitchToken("").setDiscordToken("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing discord channel")
    void incompleteOneDC(){
        b.setTwitchToken("").setDiscordToken("").setTwitchChannelName("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing twitch token")
    void incompleteOneTT(){
        b.setDiscordChannelID("").setDiscordToken("").setTwitchChannelName("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing discord token")
    void incompleteOneDT(){
        b.setTwitchToken("").setDiscordChannelID("").setTwitchChannelName("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("build")
    void build(){
        b.setTwitchToken("token of twitch").setDiscordChannelID("id").setDiscordToken("token of discord").setTwitchChannelName("name");
        BridgeConfig c = b.build();
        assertEquals("token of twitch", c.getTwitchToken());
        assertEquals( "token of discord", c.getDiscordToken());
        assertEquals("id", c.getDiscordChannelID());
        assertEquals("name", c.getTwitchChannelName());
    }

}
