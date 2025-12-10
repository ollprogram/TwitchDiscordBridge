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
package fr.ollprogram.twitchdiscordbridge.configuration.build;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfigImpl;
import fr.ollprogram.twitchdiscordbridge.exception.IncompleteConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of a configuration builder
 */
public class ConfigBuilderImpl implements ConfigBuilder {

    private String twitchToken;
    private String discordToken;

    private String discordChannelID;

    private String twitchChannelName;

    /**
     * Constructor
     */
    public ConfigBuilderImpl(){
    }

    /**
     * Constructor to start from an existing configuration
     * @param config The existing configuration
     */
    public ConfigBuilderImpl(BridgeConfig config){
        this.setDiscordChannelID(config.getDiscordChannelID())
                .setDiscordToken(config.getDiscordToken())
                .setTwitchChannelName(config.getTwitchChannelName())
                .setTwitchToken(config.getTwitchToken());
    }

    @Override
    public @NotNull ConfigBuilder setDiscordChannelID(String id) {
        this.discordChannelID = id;
        return this;
    }

    @Override
    public @NotNull ConfigBuilder setTwitchToken(String token) {
        twitchToken = token;
        return this;
    }

    @Override
    public @NotNull ConfigBuilder setDiscordToken(String token) {
        discordToken = token;
        return this;
    }

    @Override
    public @NotNull ConfigBuilder setTwitchChannelName(String name) {
        this.twitchChannelName = name;
        return this;
    }

    @Override
    public boolean isComplete() {
        return twitchChannelName != null && discordChannelID != null && discordToken != null && twitchToken != null;
    }

    @Override
    public @NotNull BridgeConfig build() {
        if(isComplete()) return new BridgeConfigImpl(twitchChannelName, discordChannelID, twitchToken, discordToken);
        else throw new IncompleteConfigurationException("Incomplete number of fields, unable to build");
    }

    @Nullable
    @Override
    public String getTwitchToken() {
        return twitchToken;
    }

    @Nullable
    @Override
    public String getDiscordToken() {
        return discordToken;
    }

    @Nullable
    @Override
    public String getDiscordChannelID() {
        return discordChannelID;
    }

    @Nullable
    @Override
    public String getTwitchChannelName() {
        return twitchChannelName;
    }
}
