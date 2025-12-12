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
import com.github.twitch4j.chat.TwitchChat;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaver;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaverToProps;
import fr.ollprogram.twitchdiscordbridge.exception.BridgeNotOpenedException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceException;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import fr.ollprogram.twitchdiscordbridge.service.TwitchService;
import fr.ollprogram.twitchdiscordbridge.service.TwitchServiceImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Implementation of a Bridge.
 * Because some most of the methods can be called by different threads, some blocks are synchronised to avoid race conditions.
 *
 */
public class BridgeImpl implements Bridge {

    private final JDA discordBot;

    private boolean open;

    private final TwitchClient twitchBot;

    private final BridgeConfig config;

    private static final Logger LOG = LoggerFactory.getLogger("Bridge");

    /**
     * Constructor
     * @param discordBot The discord bot / JDA instance
     * @param twitchBot The twitch client
     * @param config The bridge configuration
     */
    public BridgeImpl(@NotNull JDA discordBot, @NotNull TwitchClient twitchBot, @NotNull BridgeConfig config){
        this.config = config;
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
        if(!isOpen()) throw new BridgeNotOpenedException("The bridge should be opened before using this operation");
        twitchBot.getChat().sendMessage(getTwitchChannelNameSync(), message);
    }

    @Override
    public void sendToDiscord(@NotNull String message) {
        if(!isOpen()) throw new BridgeNotOpenedException("The bridge should be opened before using this operation");
        TextChannel channel = discordBot.getTextChannelById(getDiscordChannelIDSync());
        if(channel == null){
            LOG.warn("Discord channel not found (configuration is outdated)");
            return;
        }
        channel.sendMessage(message).queue();
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

    @Override
    public boolean changeTwitchChannel(@NotNull String channelName) {
        //Not using Helix form Twitch4J since the interface creates warning with URL and is too much complex for this easy task, but easy to change if necessary
        TwitchService service = new TwitchServiceImpl();
        Optional<TwitchChannelInfo> channelOpt;

        try{
            service.authenticate(config.getTwitchToken());
            channelOpt = service.getChannel(channelName);
        } catch (ServiceException e){
            LOG.warn("Operation aborted. Request failed due to : "+e.getMessage());
            return false;
        }
        if(channelOpt.isEmpty()) return false;
        TwitchChat chat = twitchBot.getChat();
        synchronized (this){
            chat.leaveChannel(config.getTwitchChannelName());
            chat.joinChannel(channelName);
            config.changeTwitchChannelName(channelName);
            LOG.info("Changed the twitch channel to ["+channelName+"].");
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

    @Override
    public boolean isDiscordTarget(@NotNull String channelID) {
        return channelID.equals(getDiscordChannelIDSync());
    }

    @Override
    public boolean isTwitchTarget(@NotNull String channelName) {
        return channelName.equals(getTwitchChannelNameSync());
    }

    @Override
    public synchronized @NotNull BridgeConfig getConfig() {
        ConfigBuilder builder = new ConfigBuilderImpl(config);
        return builder.build(); //return a copy
    }

    /**
     * Synchronized method to get the discord channel ID atomically
     * @return the discord channel ID
     */
    private synchronized String getDiscordChannelIDSync(){
        return config.getDiscordChannelID();
    }

    /**
     * Synchronized method to get the twitch channel name atomically
     * @return the twitch channel name
     */
    private synchronized String getTwitchChannelNameSync(){
        return config.getTwitchChannelName();
    }

}
