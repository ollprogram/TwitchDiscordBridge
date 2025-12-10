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

package fr.ollprogram.twitchdiscordbridge.configuration.save;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfigImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.logging.Logger.getGlobal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigSaverTest {
    private static final String PROPERTIES_FILE = "save.properties";

    private static final String TEST_FILES_ROOT = "src/test/resources/configFactory/";

    private static final String TEST_PATHNAME = TEST_FILES_ROOT + PROPERTIES_FILE;

    private static void assertConfigurationFile(BridgeConfig expected, String pathname)
            throws IOException {
        Properties props = new Properties();
        InputStream is = new FileInputStream(pathname);
        props.load(is);
        is.close();
        assertEquals(expected.getDiscordChannelID(), props.getProperty("DiscordChannelID"));
        assertEquals(expected.getTwitchChannelName(),props.getProperty("TwitchChannelName"));
        assertEquals(expected.getTwitchToken(),props.getProperty("TwitchToken"));
        assertEquals(expected.getDiscordToken(), props.getProperty("DiscordToken"));
    }

    @AfterEach
    void afterEach(){
        File f = new File(PROPERTIES_FILE);
        if(f.isFile() && f.exists()){
            if(!f.delete()) getGlobal().warning("Tested File "+PROPERTIES_FILE+" can't be deleted.");
        }
    }

    @Test
    @DisplayName("Saving a configuration")
    void saving1() throws IOException {
        BridgeConfig b = new BridgeConfigImpl("my_twitch_channel", "1006321562229678111",
                "my%twitch%token", "my%discord%token");
        ConfigSaver saver = new ConfigSaverToProps(b);
        saver.save(TEST_PATHNAME);
        assertConfigurationFile(b, TEST_PATHNAME);
    }

}
