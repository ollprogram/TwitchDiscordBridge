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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ollprogram.twitchdiscordbridge.auth.BotAuthService;
import fr.ollprogram.twitchdiscordbridge.auth.BotInfo;
import fr.ollprogram.twitchdiscordbridge.auth.DiscordAuthService;
import fr.ollprogram.twitchdiscordbridge.auth.TwitchAuthService;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * The bridge configuration validator implementation
 *
 * @author ollprogram
 */
public class ConfigValidatorImpl implements ConfigValidator {

    private final BridgeConfig bridgeConfig;
    private final BotAuthService twitchAuthService;
    private final BotAuthService discordAuthService;

    private final Logger logger;

    public ConfigValidatorImpl(BridgeConfig bridgeConfig){
        this.bridgeConfig = bridgeConfig;
        this.logger = Logger.getLogger("BridgeConfigValidator");
        this.discordAuthService = new DiscordAuthService();
        this.twitchAuthService = new TwitchAuthService();
    }

    public ConfigValidatorImpl(ConfigBuilder configBuilder){
        this(configBuilder.build());
    }
    @Override
    public boolean isValid() {
        logger.info("Checking both token validity");
        String discordToken = bridgeConfig.getDiscordToken();
        String twitchToken = bridgeConfig.getTwitchToken();
        logger.info("Checking discord token validity...");
        Optional<BotInfo> discordInfo = discordAuthService.authenticate(discordToken);
        if(discordInfo.isEmpty()) {
            logger.info("Discord token is invalid");
            return false;
        }
        else {
            logger.info("Discord token is valid, retrieved bot : "+discordInfo.get());
        }
        logger.info("Checking twitch token validity...");
        Optional<BotInfo> twitchInfo = discordAuthService.authenticate(twitchToken);
        if(twitchInfo.isEmpty()) {
            logger.info("Twitch token is invalid");
            return false;
        }
        else {
            logger.info("Twitch token is valid, retrieved bot : "+twitchInfo.get());
        }
        return true;
    }
}
