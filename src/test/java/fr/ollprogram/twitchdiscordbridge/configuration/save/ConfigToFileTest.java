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

package fr.ollprogram.twitchdiscordbridge.configuration.save;

import fr.ollprogram.twitchdiscordbridge.configuration.BConf;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.logging.Logger.getGlobal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigToFileTest {
    private static final String PROPERTIES_FILE = "bridge.properties";

    private ConfigToFile saver;

    private static void assertConfigurationFile(BridgeConfig expected)
            throws IOException {
        Properties props = new Properties();
        InputStream is = new FileInputStream(PROPERTIES_FILE);
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
        boolean deleted = false;
        if(f.isFile() && f.exists()){
            deleted = f.delete();
            if(!deleted) getGlobal().warning("Tested File "+PROPERTIES_FILE+" can't be deleted.");
        }
    }
    @BeforeEach
    void beforeEach(){
        saver = new ConfigToProps();
    }

    @Test
    @DisplayName("Saving a configuration")
    void saving1() throws IOException {
        BridgeConfig b = new BConf("my_twitch_channel", "1006321562229678111",
                "my%twitch%token", "my%discord%token");
        saver.saveConfiguration(b);
        assertConfigurationFile(b);
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Saving a null configuration")
    void savingNull() {
        assertThrows(IllegalArgumentException.class, () -> saver.saveConfiguration(null));
    }

}
