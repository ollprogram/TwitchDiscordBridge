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

package fr.ollprogram.twitchdiscordbridge.factory;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotFactoryImpl implements BotFactory{

    private final BridgeConfig config;

    private static final Logger LOG = LoggerFactory.getLogger("BotFactory");

    public BotFactoryImpl(BridgeConfig config){
        this.config = config;
    }

    @Override
    public @NotNull JDA createDiscordBot() {
        JDA jda = JDABuilder.createDefault(config.getDiscordToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .build();
        try {
            jda.awaitReady(); //wait full start
        } catch (InterruptedException e) {
            LOG.error("Something went wrong while waiting JDA connection : "+e.getMessage());
            System.exit(1);
        }
        return jda;
    }

    @Override
    public @NotNull TwitchClient createTwitchBot() {
        OAuth2Credential twitchCred = new OAuth2Credential("twitch", config.getTwitchToken());
        TwitchClient client = TwitchClientBuilder.builder()
                .withChatAccount(twitchCred)
                .withEnableChat(true)
                .build();
        client.getChat().joinChannel(config.getTwitchChannelName());
        return client;
    }
}
