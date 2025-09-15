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
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConfigValidatorTest {

    private ConfigValidator validator;

    private ConfigBuilder configBuilder;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class FakeResponse{

        public String login = "ollprogram";
        public String user_id;
        public long expires_in;
        public String id;
        public String name;
    }

    @BeforeEach
    void before(){
        configBuilder.setTwitchChannelName("ollprogram");
        configBuilder.setDiscordChannelID("1234");
        configBuilder.setDiscordToken("discord_token");
        configBuilder.setTwitchToken("twitch_token");
        validator = new ConfigValidatorImpl(configBuilder);
    }
    @Test
    void isValid() throws IOException, InterruptedException {
        //TODO
    }


}