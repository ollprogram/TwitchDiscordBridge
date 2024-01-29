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
import org.apache.commons.lang.IncompleteArgumentException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

/**
 * Implementation of a ConfigFromFile which loads from properties files.
 * @author ollprogram
 */
public class ConfigFromProps implements ConfigFromFile {

    private static final String PROPERTIES = ".properties";

    private final Properties props;

    private final BridgeConfigBuilder builder;

    /**
     * Constructor, using the default path
     * @param builder The bridge config builder
     */
    public ConfigFromProps(@NotNull BridgeConfigBuilder builder){
        this.builder = builder;
        props = new Properties();
    }

    private void loadProps(){
        builder.setTwitchChannelName(props.getProperty("TwitchChannelName"))
                .setTwitchToken(props.getProperty("TwitchToken"))
                .setDiscordToken(props.getProperty("DiscordToken"))
                .setDiscordChannelID(props.getProperty("DiscordChannelID"));
    }

    @Override
    public void load() throws IOException {
        InputStream is;
        is = new FileInputStream(DEFAULT_FILE_NAME + PROPERTIES);
        props.load(is);
        is.close();
        loadProps();
    }

    @Override
    public void load(@NotNull String pathname) throws IOException {
        InputStream is = getClass().getResourceAsStream(pathname);
        if (is == null) throw new FileNotFoundException("Can't find the properties file " + pathname);
        props.load(is);
        is.close();
        loadProps();
    }

    @Override
    public boolean isComplete() {
        return builder.isComplete();
    }

    @Override
    public @NotNull BridgeConfig createConfiguration() throws IncompleteArgumentException {
        if(!isComplete()) throw new IncompleteArgumentException("Can't create an incomplete configuration");
        return builder.build();
    }
}
