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
import fr.ollprogram.twitchdiscordbridge.configuration.builder.BridgeConfigBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.logging.Logger.getGlobal;

public class ConfigFromProps implements ConfigFromFile {

    private static final String PROPERTIES_FILE = "bridge.properties";

    private Properties props;

    private final BridgeConfigBuilder builder;

    /**
     * Constructor, search the file into resources
     * @param resourcePath The properties file pathname
     */
    public ConfigFromProps(@NotNull String resourcePath, BridgeConfigBuilder builder){
        this.builder = builder;
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if(is == null) throw new IOException("Resource not available");
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException e) {
            getGlobal().warning("Unable to load the properties file");
        }
        tryLoadProps();
    }

    /**
     * Constructor, using the default path
     */
    public ConfigFromProps(BridgeConfigBuilder builder){
        this.builder = builder;
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_FILE);
            props = new Properties();
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            getGlobal().warning("Unable to load the properties file");
        }
        tryLoadProps();
    }

    private void tryLoadProps(){
        builder.setTwitchChannelName(props.getProperty("TwitchChannelName"));
        builder.setTwitchToken(props.getProperty("TwitchToken"));
        builder.setDiscordToken(props.getProperty("DiscordToken"));
        builder.setDiscordChannelID(props.getProperty("DiscordChannelID"));
    }

    @Override
    public boolean canLoadConfiguration() {
        return builder.isComplete();
    }

    @Override
    public BridgeConfig loadConfiguration() throws IOException {
        if(builder.isComplete()) return builder.build();
        else throw new IOException("Can't load configuration file");
    }

}
