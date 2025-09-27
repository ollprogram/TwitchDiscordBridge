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

import fr.ollprogram.twitchdiscordbridge.model.DiscordChannelInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import fr.ollprogram.twitchdiscordbridge.service.*;
import fr.ollprogram.twitchdiscordbridge.service.TwitchServiceImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.model.DiscordBotInfo;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * The bridge configuration validator implementation
 *
 * @author ollprogram
 */
public class ConfigValidatorImpl implements ConfigValidator {

    private final BridgeConfig bridgeConfig;
    private final TwitchService twitchService;
    private final DiscordService discordService;

    private final Logger logger;

    /**
     * Validator constructor
     * @param bridgeConfig The bridge configuration
     */
    public ConfigValidatorImpl(BridgeConfig bridgeConfig){
        this.bridgeConfig = bridgeConfig;
        this.logger = Logger.getLogger("BridgeConfigValidator");
        this.discordService = new DiscordServiceImpl();
        this.twitchService = new TwitchServiceImpl();
    }

    /**
     * Validator constructor
     * @param configBuilder Bridge configuration builder
     */
    public ConfigValidatorImpl(ConfigBuilder configBuilder){
        this(configBuilder.build());
    }

    @Override
    public boolean isValid() {
        return isValidDiscordToken() && isValidDiscordChannelID() && isValidTwitchToken() && isValidTwitchChannelName();
    }

    /**
     * Check discord token validity
     * @return If the discord token is valid
     */
    private boolean isValidDiscordToken() {
        String discordToken = bridgeConfig.getDiscordToken();
        logger.info("Checking discord token validity...");
        Optional<DiscordBotInfo> discordInfo = discordService.authenticate(discordToken);
        if(discordInfo.isEmpty()) {
            logger.info("Discord token is invalid");
            return false;
        }
        logger.info("Discord token is valid, retrieved bot : "+discordInfo.get());
        return true;
    }

    /**
     * Check the twitch token validity
     * @return If the twitch token is valid
     */
    private boolean isValidTwitchToken() {
        String twitchToken = bridgeConfig.getTwitchToken();
        logger.info("Checking twitch token validity...");
        Optional<TwitchBotInfo> twitchInfo = twitchService.authenticate(twitchToken);
        if(twitchInfo.isEmpty()) {
            logger.info("Twitch token is invalid");
            return false;
        }
        logger.info("Twitch token is valid, retrieved bot : "+twitchInfo.get());
        return true;
    }

    /**
     * Check the twitch channel name validity
     * @return If the twitch channel name is valid
     */
    private boolean isValidTwitchChannelName(){
        String twitchChannelName = bridgeConfig.getTwitchChannelName();
        logger.info("Checking twitch channel validity...");
        Optional<TwitchChannelInfo> twitchChannelInfo = twitchService.getChannel(twitchChannelName);
        if(twitchChannelInfo.isEmpty()) {
            logger.info("Twitch channel name is invalid");
            return false;
        }
        logger.info("Twitch channel retrieved : "+twitchChannelInfo.get());
        return true;
    }

    /**
     * Check the discord channel ID validity
     * @return If the discord channel is valid
     */
    private boolean isValidDiscordChannelID(){
        String discordChannelID = bridgeConfig.getDiscordChannelID();
        logger.info("Checking discord channel validity...");
        Optional<DiscordChannelInfo> channelInfo = discordService.getChannel(discordChannelID);
        if(channelInfo.isEmpty()) {
            logger.info("Discord channel ID is invalid");
            return false;
        }
        logger.info("Discord channel retrieved : "+channelInfo.get());
        return true;
    }
}
