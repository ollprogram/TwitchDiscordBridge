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

package fr.ollprogram.twitchdiscordbridge.configuration.factory;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.builder.BConfBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.builder.BridgeConfigBuilder;
import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigFromFileTest {

    private ConfigFromFile fact;

    @BeforeEach
    void before(){
        BridgeConfigBuilder builder = new BConfBuilder();
        fact = new ConfigFromProps(builder);
    }

    @Test
    @Tag("Robustness")
    @DisplayName("missing builder constructor param")
    void nullConstructor1(){
        assertThrows(IllegalArgumentException.class, () -> new ConfigFromProps(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("null resource name")
    void nullLoad(){
        assertThrows(IllegalArgumentException.class, () -> fact.load(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("resource not found")
    void notFoundRes(){
        assertThrows(FileNotFoundException.class, () -> fact.load("unknown"));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("default file not found")
    void notFound(){
        assertThrows(FileNotFoundException.class, () -> fact.load());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props empty create")
    void createBad1() throws IOException {
        fact.load("/configFactory/empty.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete props empty")
    void incomplete1() throws IOException {
        fact.load("/configFactory/empty.properties");
        assertFalse(fact.isComplete());
    }


    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props empty create")
    void createBad2() throws IOException {
        fact.load("/configFactory/randomEmpty.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete props empty")
    void incomplete2() throws IOException {
        fact.load("/configFactory/randomEmpty.properties");
        assertFalse(fact.isComplete());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props missing Twitch token. create")
    void createBad3() throws IOException {
        fact.load("/configFactory/missingTT.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete missing twitch token")
    void incomplete3() throws IOException {
        fact.load("/configFactory/missingTT.properties");
        assertFalse(fact.isComplete());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props missing discord token. create")
    void createBad4() throws IOException {
        fact.load("/configFactory/missingDT.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete missing discord token")
    void incomplete4() throws IOException {
        fact.load("/configFactory/missingDT.properties");
        assertFalse(fact.isComplete());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props missing discord channel. create")
    void createBad5() throws IOException {
        fact.load("/configFactory/missingDC.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete missing discord channel")
    void incomplete5() throws IOException {
        fact.load("/configFactory/missingDC.properties");
        assertFalse(fact.isComplete());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props missing twitch channel. create")
    void createBad6() throws IOException {
        fact.load("/configFactory/missingTC.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete missing twitch channel")
    void incomplete6() throws IOException {
        fact.load("/configFactory/missingTC.properties");
        assertFalse(fact.isComplete());
    }

    @Test
    @DisplayName("Completed config")
    void complete1() throws IOException {
        fact.load("/configFactory/completed.properties");
        assertTrue(fact.isComplete());
    }

    @Test
    @DisplayName("Completed config create")
    void completeCreate1() throws IOException {
        fact.load("/configFactory/completed.properties");
        BridgeConfig b = fact.createConfiguration();
        assertEquals("my%twitch%token", b.getTwitchToken());
        assertEquals("my%discord%token", b.getDiscordToken());
        assertEquals("my_twitch_channel", b.getTwitchChannelName());
        assertEquals("1006321562229678111", b.getDiscordChannelID());
    }
}
