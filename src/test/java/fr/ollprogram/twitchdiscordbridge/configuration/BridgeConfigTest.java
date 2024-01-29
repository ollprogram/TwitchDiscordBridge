/* Copyright © 2024 ollprogram
 *
 * This file is part of TwitchDiscordBridge.
 * TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or \(at your option\) any later version.
 * TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
 * If not, see https://www.gnu.org/licenses.
 */

package fr.ollprogram.twitchdiscordbridge.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BridgeConfigTest {
    @Test
    @Tag("Robustness")
    @DisplayName("all null")
    void allNull(){
        assertThrows(IllegalArgumentException.class, () -> new BConf(null, null, null, null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("discord token is null")
    void nullDT(){
        assertThrows(IllegalArgumentException.class, () -> new BConf("", "", "", null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("twitch token is null")
    void nullTT(){
        assertThrows(IllegalArgumentException.class, () -> new BConf("", "", null, ""));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("discord channel is null")
    void nullDC(){
        assertThrows(IllegalArgumentException.class, () -> new BConf("", null, "", ""));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("twitch channel is null")
    void nullTC(){
        assertThrows(IllegalArgumentException.class, () -> new BConf(null, "", "", ""));
    }

    private BridgeConfig bc;

    @BeforeEach
    void before(){
        bc = new BConf("name", "id", "twitch", "discord");
    }

    @Test
    void getTwitchChannelName() {
        assertEquals("name", bc.getTwitchChannelName());
    }

    @Test
    void getDiscordChannelID() {
        assertEquals("id", bc.getDiscordChannelID());
    }

    @Test
    void getTwitchToken() {
        assertEquals("twitch", bc.getTwitchToken());
    }

    @Test
    void getDiscordToken() {
        assertEquals("discord", bc.getDiscordToken());
    }

    @Test
    void changeDiscordChannelID() {
        bc.changeDiscordChannelID("id2");
        assertEquals("id2", bc.getDiscordChannelID());
    }

    @Test
    void changeTwitchChannelName() {
        bc.changeTwitchChannelName("name2");
        assertEquals("name2", bc.getTwitchChannelName());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("changing twitch channel to null")
    void changeNullTC(){
        assertThrows(IllegalArgumentException.class, () -> bc.changeTwitchChannelName(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("changing discord channel to null")
    void changeNullDC(){
        assertThrows(IllegalArgumentException.class, () -> bc.changeDiscordChannelID(null));
    }
}
