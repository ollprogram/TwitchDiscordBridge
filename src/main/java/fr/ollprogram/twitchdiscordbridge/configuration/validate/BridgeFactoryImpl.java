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

package fr.ollprogram.twitchdiscordbridge.configuration.validate;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import fr.ollprogram.twitchdiscordbridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.BridgeImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.exception.InvalidConfigurationException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.jetbrains.annotations.NotNull;

import static java.util.logging.Logger.getGlobal;


/**
 * Implementation of a bridge factory.
 * @author ollprogram
 */
public class BridgeFactoryImpl implements BridgeFactory {

    private JDA discordBot;

    private TwitchClient twitchBot;

    private final BridgeConfig conf;


    public BridgeFactoryImpl(BridgeConfig conf){
        this.discordBot = null;
        this.twitchBot = null;
        this.conf = conf;
    }

    public void loadConfiguration() {
        //please change in next version with an oauth2 validator
        discordBot = JDABuilder.createDefault(conf.getDiscordToken()).build();
        OAuth2Credential twitchCred = new OAuth2Credential("twitch", conf.getTwitchToken());
        TwitchClientBuilder builder = TwitchClientBuilder.builder().withChatAccount(twitchCred);
        twitchBot = builder.build();
        //TODO
    }

    @Override
    public @NotNull Bridge createBridge() {
        return null;
    }
}
