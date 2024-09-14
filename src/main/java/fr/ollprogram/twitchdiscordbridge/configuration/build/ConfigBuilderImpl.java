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
package fr.ollprogram.twitchdiscordbridge.configuration.build;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfigImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a configuration builder
 * @author ollprogram
 */
public class ConfigBuilderImpl implements ConfigBuilder {

    private String tToken;
    private String dToken;

    private String id;

    private String name;

    @Override
    public @NotNull ConfigBuilder setDiscordChannelID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public @NotNull ConfigBuilder setTwitchToken(String token) {
        tToken = token;
        return this;
    }

    @Override
    public @NotNull ConfigBuilder setDiscordToken(String token) {
        dToken = token;
        return this;
    }

    @Override
    public @NotNull ConfigBuilder setTwitchChannelName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean isComplete() {
        return name != null && id != null && dToken != null && tToken != null;
    }

    @Override
    public @NotNull BridgeConfig build() {
        if(isComplete()) return new BridgeConfigImpl(name, id, tToken, dToken);
        else throw new IllegalArgumentException("Incomplete number of fields");
    }
}
