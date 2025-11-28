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

package fr.ollprogram.twitchdiscordbridge.bridge;

import com.github.twitch4j.TwitchClient;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaver;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaverToProps;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Implementation of a Bridge
 *
 * @author ollprogram
 */
public class BridgeImpl implements Bridge {

    private final JDA discordBot;

    private boolean open;

    private final TwitchClient twitchBot;

    private final BridgeConfig config;

    private static final Logger LOG = LoggerFactory.getLogger("Bridge");


    public BridgeImpl(JDA discordBot, TwitchClient twitchBot, BridgeConfig bc){
        this.config = bc;
        this.twitchBot = twitchBot;
        this.discordBot = discordBot;
        this.open = false;
    }

    @Override
    public synchronized boolean isOpen() {
        return open;
    }

    @Override
    public synchronized void close() {
        open = false;
    }

    @Override
    public synchronized void open() {
        open = true;
    }

    @Override
    public void sendToTwitch(@NotNull String message) {
        twitchBot.getChat().sendMessage(config.getTwitchChannelName(), message);
    }

    @Override
    public void sendToDiscord(@NotNull String message) {
        TextChannel channel = discordBot.getTextChannelById(config.getDiscordChannelID());
        if(channel == null){
            LOG.warn("Discord channel not found (configuration is outdated)");
            return;
        }
        channel.sendMessage(message).queue();
    }

    @Override
    public @NotNull BridgeConfig getConfig() {
        return config;
    }

    @Override
    public boolean changeDiscordChannel(@NotNull String channelID) {
        TextChannel channel = discordBot.getTextChannelById(channelID);
        if(channel == null) return false;
        synchronized (this){
            config.changeDiscordChannelID(channelID);
            LOG.info("Changed the discord channel to ["+channel.getName()+"].");
            LOG.info("Saving configuration");
            ConfigSaver saver = new ConfigSaverToProps(config);
            try {
                saver.save();
            } catch (IOException e) {
                LOG.warn("Configuration can't be saved");
            }
            LOG.info("Configuration saved");
        }
        return true;
    }

}
