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

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceException;
import fr.ollprogram.twitchdiscordbridge.model.DiscordBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.DiscordChannelInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import fr.ollprogram.twitchdiscordbridge.service.DiscordService;
import fr.ollprogram.twitchdiscordbridge.service.DiscordServiceImpl;
import fr.ollprogram.twitchdiscordbridge.service.TwitchService;
import fr.ollprogram.twitchdiscordbridge.service.TwitchServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * The bridge configuration validator implementation
 *
 * @author ollprogram
 */
public class ConfigValidatorImpl implements ConfigValidator {

    private final BridgeConfig bridgeConfig;
    private final TwitchService twitchService;
    private final DiscordService discordService;

    private static final Logger LOG = LoggerFactory.getLogger("BridgeConfigValidator");;

    /**
     * Validator constructor
     * @param bridgeConfig The bridge configuration
     * @param discordService The discord service
     * @param twitchService The twitch service
     */
    public ConfigValidatorImpl(BridgeConfig bridgeConfig, TwitchService twitchService, DiscordService discordService){
        this.bridgeConfig = bridgeConfig;
        this.discordService = discordService;
        this.twitchService = twitchService;
    }

    /**
     * Validator constructor
     * @param configBuilder Bridge configuration builder
     * @param discordService The discord service
     * @param twitchService The twitch service
     */
    public ConfigValidatorImpl(ConfigBuilder configBuilder, TwitchService twitchService, DiscordService discordService){
        this(configBuilder.build(), twitchService, discordService);
    }

    /**
     * Validator constructor with default services
     * @param configBuilder Bridge configuration builder
     */
    public ConfigValidatorImpl(ConfigBuilder configBuilder){
        this(configBuilder.build(), new TwitchServiceImpl(), new DiscordServiceImpl());
    }

    @Override
    public boolean isValid() {
        try {
            return isValidDiscordToken() && isValidDiscordChannelID() && isValidTwitchToken() && isValidTwitchChannelName();
        } catch(ServiceException e){
            LOG.error("An error occurs during validation : "+e.getMessage());
            System.exit(1);
        }
        return false;
    }

    /**
     * Check discord token validity
     * @return If the discord token is valid
     */
    private boolean isValidDiscordToken() throws ServiceException {
        String discordToken = bridgeConfig.getDiscordToken();
        LOG.info("Checking discord token validity...");
        Optional<DiscordBotInfo> discordInfo = discordService.authenticate(discordToken);
        if(discordInfo.isEmpty()) {
            LOG.info("Discord token is invalid");
            return false;
        }
        LOG.info("Discord token is valid, retrieved bot : "+discordInfo.get());
        return true;
    }

    /**
     * Check the twitch token validity
     * @return If the twitch token is valid
     */
    private boolean isValidTwitchToken() throws ServiceException {
        String twitchToken = bridgeConfig.getTwitchToken();
        LOG.info("Checking twitch token validity...");
        Optional<TwitchBotInfo> twitchInfo = twitchService.authenticate(twitchToken);
        if(twitchInfo.isEmpty()) {
            LOG.info("Twitch token is invalid");
            return false;
        }
        LOG.info("Twitch token is valid, retrieved bot : "+twitchInfo.get());
        return true;
    }

    /**
     * Check the twitch channel name validity
     * @return If the twitch channel name is valid
     */
    private boolean isValidTwitchChannelName() throws ServiceException {
        String twitchChannelName = bridgeConfig.getTwitchChannelName();
        LOG.info("Checking twitch channel validity...");
        Optional<TwitchChannelInfo> twitchChannelInfo = twitchService.getChannel(twitchChannelName);
        if(twitchChannelInfo.isEmpty()) {
            LOG.info("Twitch channel name is invalid");
            return false;
        }
        LOG.info("Twitch channel retrieved : "+twitchChannelInfo.get());
        return true;
    }

    /**
     * Check the discord channel ID validity
     * @return If the discord channel is valid
     */
    private boolean isValidDiscordChannelID() throws ServiceException {
        String discordChannelID = bridgeConfig.getDiscordChannelID();
        LOG.info("Checking discord channel validity...");
        Optional<DiscordChannelInfo> channelInfo = discordService.getChannel(discordChannelID);
        if(channelInfo.isEmpty()) {
            LOG.info("Discord channel ID is invalid");
            return false;
        }
        LOG.info("Discord channel retrieved : "+channelInfo.get());
        return true;
    }
}
