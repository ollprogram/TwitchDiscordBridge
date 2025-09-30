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

import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.model.DiscordBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.DiscordChannelInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import fr.ollprogram.twitchdiscordbridge.service.DiscordService;
import fr.ollprogram.twitchdiscordbridge.service.TwitchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConfigValidatorTest {

    private static final DiscordBotInfo VALID_DISCORD_BOT_INFO = new DiscordBotInfo("23", "my_bot");

    private static final TwitchBotInfo VALID_TWITCH_BOT_INFO = new TwitchBotInfo("23", new Date());

    private static final DiscordChannelInfo VALID_DISCORD_CHANNEL_INFO = new DiscordChannelInfo("12", "my_channel");

    private static final TwitchChannelInfo VALID_TWITCH_CHANNEL_INFO = new TwitchChannelInfo("22323", "my_channel");


    private ConfigValidator validator;
    private DiscordService discordService;
    private TwitchService twitchService;

    @BeforeEach
    void before(){
        ConfigBuilder configBuilder = new ConfigBuilderImpl();
        configBuilder.setTwitchChannelName("ollprogram");
        configBuilder.setDiscordChannelID("1234");
        configBuilder.setDiscordToken("discord_token");
        configBuilder.setTwitchToken("twitch_token");
        discordService = mock(DiscordService.class);
        twitchService = mock(TwitchService.class);
        validator = new ConfigValidatorImpl(configBuilder, twitchService, discordService);
    }

    /**
     * Mock all service calls
     * @param twitchTokenValidity if the twitch token will be returned as valid
     * @param discordTokenValidity if the discord token will be returned as valid
     * @param twitchChannelValidity if the twitch channel will be returned as valid
     * @param discordChannelValidity if the discord channel will be returned as valid
     */
    private void mockAllServiceCalls(boolean twitchTokenValidity, boolean discordTokenValidity, boolean twitchChannelValidity, boolean discordChannelValidity){
        when(discordService.authenticate(any())).thenReturn(Optional.ofNullable(discordTokenValidity? VALID_DISCORD_BOT_INFO : null));
        when(twitchService.authenticate(any())).thenReturn(Optional.ofNullable(twitchTokenValidity? VALID_TWITCH_BOT_INFO : null));
        when(discordService.getChannel(any())).thenReturn(Optional.ofNullable(discordChannelValidity? VALID_DISCORD_CHANNEL_INFO : null));
        when(twitchService.getChannel(any())).thenReturn(Optional.ofNullable(twitchChannelValidity? VALID_TWITCH_CHANNEL_INFO: null));
    }

    @Test
    @DisplayName("all valid")
    void isValid() {
        mockAllServiceCalls(true, true, true, true);
        assertTrue(validator.isValid());
    }

    @Test
    @DisplayName("all invalid")
    void isInvalid() {
        mockAllServiceCalls(false, false, false, false);
        assertFalse(validator.isValid());
    }

    @Test
    @DisplayName("twitch token is invalid")
    void isInvalidTwitchToken() {
        mockAllServiceCalls(false, true, true, true);
        assertFalse(validator.isValid());
    }

    @Test
    @DisplayName("discord token is invalid")
    void isInvalidDiscordToken() {
        mockAllServiceCalls(true, false, true, true);
        assertFalse(validator.isValid());
    }

    @Test
    @DisplayName("twitch channel is invalid")
    void isInvalidTwitchChannel() {
        mockAllServiceCalls(true, true, false, true);
        assertFalse(validator.isValid());
    }

    @Test
    @DisplayName("discord channel is invalid")
    void isInvalidDiscordChannel() {
        mockAllServiceCalls(true, true, true, false);
        assertFalse(validator.isValid());
    }



}