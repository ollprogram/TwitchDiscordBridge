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

import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

/**
 * Implementation of a ConfigFromFile which loads from properties files.
 * @author ollprogram
 */
public class ConfigLoaderFromProps implements ConfigLoader {

    private static final String PROPERTIES = ".properties";

    private final Properties props;

    private final ConfigBuilder builder;

    /**
     * Constructor, using the default path
     * @param builder The bridge config builder
     */
    public ConfigLoaderFromProps(@NotNull ConfigBuilder builder){
        this.builder = builder;
        props = new Properties();
    }

    /**
     * Load all properties
     */
    private void loadProps(){
        builder.setTwitchChannelName(props.getProperty("TwitchChannelName"))
                .setTwitchToken(props.getProperty("TwitchToken"))
                .setDiscordToken(props.getProperty("DiscordToken"))
                .setDiscordChannelID(props.getProperty("DiscordChannelID"));
    }

    @Override
    public void load() throws IOException {
        this.load(DEFAULT_FILE_NAME + PROPERTIES);
    }

    @Override
    public void load(@NotNull String pathname) throws IOException {
        InputStream is = new FileInputStream(pathname);
        props.load(is);
        is.close();
        loadProps();
    }

}
