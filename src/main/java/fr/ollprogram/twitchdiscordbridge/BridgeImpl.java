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

package fr.ollprogram.twitchdiscordbridge;

import com.github.twitch4j.TwitchClient;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a Bridge
 *
 * @author ollprogram
 */
public class BridgeImpl implements Bridge {

    private final JDA discordBot;

    private boolean shutdown;

    private final TwitchClient twitchBot;

    private final BridgeConfig config;

    private static final Logger LOG = LoggerFactory.getLogger("Bridge");


    public BridgeImpl(JDA discordBot, TwitchClient twitchBot, BridgeConfig bc){
        this.config = bc;
        this.twitchBot = twitchBot;
        this.discordBot = discordBot;
        this.shutdown = false;
    }

    @Override
    public synchronized void shutdown() throws InterruptedException {
        discordBot.shutdownNow();
        discordBot.awaitShutdown();
        twitchBot.close();
        shutdown = true;
        this.notifyAll(); //awake all threads waiting on the bridge to finish
    }

    @Override
    public void start() {
        shutdown = false;
    }

    @Override
    public synchronized boolean isShutdown() {
        return shutdown;
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
    public synchronized void awaitShutdown() throws InterruptedException {
        while(!isShutdown()) this.wait(); //passive waiting
    }
}
