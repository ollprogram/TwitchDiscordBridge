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
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * Implementation of a ConfigToFile which saves into properties files.
 * @author ollprogram
 */
public class ConfigSaverToProps implements ConfigSaver {

    private final BridgeConfig config;

    private static final String PROPERTIES = ".properties";

    public ConfigSaverToProps(@NotNull BridgeConfig config){
        this.config = config;
    }


    @Override
    public void save(@NotNull String pathname) throws IOException {
        Properties props = new Properties();
        props.put("TwitchToken", config.getTwitchToken());
        props.put("DiscordToken", config.getDiscordToken());
        props.put("TwitchChannelName", config.getTwitchChannelName());
        props.put("DiscordChannelID", config.getDiscordChannelID());
        Writer w = new FileWriter(pathname);
        props.store(w, "You can edit this file if you wish");
        w.close();
    }

    @Override
    public void save() throws IOException {
        this.save(DEFAULT_FILE_NAME + PROPERTIES);
    }
}
