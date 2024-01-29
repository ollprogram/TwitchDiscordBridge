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
package fr.ollprogram.twitchdiscordbridge.configuration.builder;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.BConf;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a configuration builder
 * @author ollprogram
 */
public class BConfBuilder implements BridgeConfigBuilder{

    private String tToken;
    private String dToken;

    private String id;

    private String name;

    @Override
    public @NotNull BridgeConfigBuilder setDiscordChannelID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public @NotNull BridgeConfigBuilder setTwitchToken(String token) {
        tToken = token;
        return this;
    }

    @Override
    public @NotNull BridgeConfigBuilder setDiscordToken(String token) {
        dToken = token;
        return this;
    }

    @Override
    public @NotNull BridgeConfigBuilder setTwitchChannelName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean isComplete() {
        return name != null && id != null && dToken != null && tToken != null;
    }

    @Override
    public @NotNull BridgeConfig build() {
        if(isComplete()) return new BConf(name, id, tToken, dToken);
        else throw new IllegalArgumentException("Incomplete number of fields");
    }
}
