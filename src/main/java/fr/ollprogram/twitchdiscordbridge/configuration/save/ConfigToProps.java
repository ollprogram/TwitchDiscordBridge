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
public class ConfigToProps implements ConfigToFile{

    private static final String PROPERTIES = ".properties";
    @Override
    public void saveConfiguration(@NotNull BridgeConfig bridgeConfig) throws IOException {
        Properties props = new Properties();
        props.put("TwitchToken", bridgeConfig.getTwitchToken());
        props.put("DiscordToken", bridgeConfig.getDiscordToken());
        props.put("TwitchChannelName", bridgeConfig.getTwitchChannelName());
        props.put("DiscordChannelID", bridgeConfig.getDiscordChannelID());
        Writer w = new FileWriter(DEFAULT_FILE_NAME + PROPERTIES);
        props.store(w, "You can edit this file if you wish");
        w.close();
    }
}
