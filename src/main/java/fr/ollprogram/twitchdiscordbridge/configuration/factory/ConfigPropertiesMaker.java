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
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfigImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.logging.Logger.getGlobal;

public class ConfigPropertiesMaker implements ConfigurationFactory{

    private static final String PROPERTIES_FILE = "bridge.properties";

    private Properties props;

    /**
     * Constructor, search the file into resources
     * @param resourcePath The properties file pathname
     */
    public ConfigPropertiesMaker(@NotNull String resourcePath){
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            getGlobal().severe("Unable to load the properties file");
            System.exit(1);
        }
    }

    /**
     * Constructor, using the default path
     */
    public ConfigPropertiesMaker(){
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_FILE);
            props = new Properties();
            props.load(fis);
        } catch (IOException e) {
            getGlobal().severe("Unable to load the properties file");
            System.exit(1);
        }
    }

    @Override
    public BridgeConfig createConfiguration() {
        String twitchName = props.getProperty("TwitchChannelName");
        String twitchToken = props.getProperty("TwitchToken");
        String discordToken = props.getProperty("DiscordToken");
        String discordChannel = props.getProperty("DiscordChannelID");
        //TODO verif + builder
        return new BridgeConfigImpl(twitchName, discordChannel, twitchToken, discordToken);
    }
}
