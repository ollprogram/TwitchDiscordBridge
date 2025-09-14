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
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a Bridge
 *
 * @author ollprogram
 */
public class BridgeImpl implements Bridge {

    private final JDA discordBot;

    private boolean shutdown;

    private final TwitchClient twitchBot;

    private final BridgeConfig bc;


    public BridgeImpl(JDA discordBot, TwitchClient twitchBot, BridgeConfig bc){
        this.bc = bc;
        this.twitchBot = twitchBot;
        this.discordBot = discordBot;
        this.shutdown = false;
        configure();
    }

    private void configure(){
        //TODO configure bots
    }

    @Override
    public void shutdown() {
        try {
            discordBot.shutdownNow();
            discordBot.awaitShutdown();
            twitchBot.close();
            shutdown = true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        //TODO
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void sendToTwitch(@NotNull String message, @NotNull String channelId) {
        //TODO
    }

    @Override
    public void sendToDiscord(@NotNull String message, @NotNull String channelId) {
        //TODO
    }
}
