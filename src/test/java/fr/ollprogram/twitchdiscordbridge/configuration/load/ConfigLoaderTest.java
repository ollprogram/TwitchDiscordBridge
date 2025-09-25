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

package fr.ollprogram.twitchdiscordbridge.configuration.load;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTest {

    private static final String TEST_FILES_ROOT = "src/test/resources/configFactory";
    private static final String DEFAULT_FILE_PATH = "bridge.properties";

    private ConfigLoader fact;
    private ConfigBuilder builder;

    @BeforeEach
    void before(){
        builder = new ConfigBuilderImpl();
        fact = new ConfigLoaderFromProps(builder);
    }

    @Test
    @Tag("Robustness")
    @DisplayName("missing builder constructor param")
    void nullConstructor1(){
        assertThrows(IllegalArgumentException.class, () -> new ConfigLoaderFromProps(null));
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
    @DisplayName("Incomplete props empty")
    void incomplete1() throws IOException {
        fact.load(TEST_FILES_ROOT+"/empty.properties");
        assertFalse(builder.isComplete());
    }

    @Test
    @DisplayName("Incomplete props empty")
    void incomplete2() throws IOException {
        fact.load(TEST_FILES_ROOT+"/randomEmpty.properties");
        assertFalse(builder.isComplete());
    }

    @Test
    @DisplayName("Incomplete missing twitch token")
    void incomplete3() throws IOException {
        fact.load(TEST_FILES_ROOT+"/missingTT.properties");
        assertFalse(builder.isComplete());
    }

    @Test
    @DisplayName("Incomplete missing discord token")
    void incomplete4() throws IOException {
        fact.load(TEST_FILES_ROOT+"/missingDT.properties");
        assertFalse(builder.isComplete());
    }

    @Test
    @DisplayName("Incomplete missing discord channel")
    void incomplete5() throws IOException {
        fact.load(TEST_FILES_ROOT+"/missingDC.properties");
        assertFalse(builder.isComplete());
    }

    @Test
    @DisplayName("Incomplete missing twitch channel")
    void incomplete6() throws IOException {
        fact.load(TEST_FILES_ROOT+"/missingTC.properties");
        assertFalse(builder.isComplete());
    }

    @Test
    @DisplayName("Completed config")
    void complete1() throws IOException {
        fact.load(TEST_FILES_ROOT+"/completed.properties");
        assertTrue(builder.isComplete());
    }

    @Test
    @DisplayName("Completed config create")
    void completeCreate1() throws IOException {
        fact.load(TEST_FILES_ROOT+"/completed.properties");
        assertEquals("my%twitch%token", builder.getTwitchToken());
        assertEquals("my%discord%token", builder.getDiscordToken());
        assertEquals("my_twitch_channel", builder.getTwitchChannelName());
        assertEquals("1006321562229678111", builder.getDiscordChannelID());
    }
}
