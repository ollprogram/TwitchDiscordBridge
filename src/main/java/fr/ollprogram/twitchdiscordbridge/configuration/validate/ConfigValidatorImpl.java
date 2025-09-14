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
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

/**
 * The bridge configuration validator implementation
 *
 * @author ollprogram
 */
public class ConfigValidatorImpl implements ConfigValidator {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TwitchResponse{

        public String login;
        public String user_id;
        public long expires_in;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DiscordResponse{
        public String id;
        public String name;
    }

    private final BridgeConfig bridgeConfig;

    private final Logger logger;

    private static final String TWITCH_BASE_ROUTE= "https://id.twitch.tv";
    private static final String DISCORD_BASE_ROUTE = "https://discord.com/api/v10";

    private static final String TWITCH_AUTH_ROUTE = "/oauth2/validate";

    private static final String DISCORD_AUTH_ROUTE = "/oauth2/applications/@me";

    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final String AUTHORIZATION_HEADER = "authorization";

    public ConfigValidatorImpl(BridgeConfig bridgeConfig){
        this.bridgeConfig = bridgeConfig;
        this.logger = Logger.getLogger("BridgeConfigValidator");
    }

    public ConfigValidatorImpl(ConfigBuilder configBuilder){
        this(configBuilder.build());
    }
    @Override
    public boolean isValid() {
        logger.info("Checking both token validity");
        String discordToken = bridgeConfig.getDiscordToken();
        String twitchToken = bridgeConfig.getTwitchToken();
        boolean validatedTokens = false;
        try {
            logger.info("Checking discord token validity...");
            boolean discordTokenValidity = isDiscordTokenValid(discordToken);
            logger.info("Discord token is " + (discordTokenValidity ? "valid" : "invalid"));
            logger.info("Checking twitch token validity...");
            boolean twitchTokenValidity = isTwitchTokenValid(twitchToken);
            logger.info("Twitch token is " + (twitchTokenValidity ? "valid" : "invalid"));
            validatedTokens = discordTokenValidity && twitchTokenValidity;
        } catch (IOException | InterruptedException e) {
            logger.severe("Unable to send a validation request");
            System.exit(1);
        }
        return validatedTokens;
    }

    private HttpRequest getDiscordRequest(String token){
        return HttpRequest.newBuilder().GET()
                .uri(URI.create(DISCORD_BASE_ROUTE + DISCORD_AUTH_ROUTE))
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"Bot "+token)
                .build();
    }

    private HttpRequest getTwitchRequest(String token){
        return HttpRequest.newBuilder().GET()
                .uri(URI.create(TWITCH_BASE_ROUTE + TWITCH_AUTH_ROUTE))
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"OAuth "+token)
                .build();
    }

    private boolean isDiscordTokenValid(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest discordRequest = getDiscordRequest(token);
        HttpResponse<String> response = client.send(discordRequest, HttpResponse.BodyHandlers.ofString());
        boolean isValid = isOk(response);
        if(isValid){
            ObjectMapper mapper = new ObjectMapper();
            try {
                DiscordResponse parsedResponse = mapper.readValue(response.body(), DiscordResponse.class);
                logger.info("Discord bot id : " + parsedResponse.id
                        + "\nDiscord bot name : " + parsedResponse.name);
            } catch (JsonProcessingException e) {
                logger.severe("Unable to read the Discord validation response.");
                System.exit(1);
            }
        }
        return isValid;
    }

    private boolean isTwitchTokenValid(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest twitchRequest = getTwitchRequest(token);
        HttpResponse<String> response = client.send(twitchRequest, HttpResponse.BodyHandlers.ofString());
        boolean isValid = isOk(response);
        if(isValid){
            ObjectMapper mapper = new ObjectMapper();
            try {
                TwitchResponse parsedResponse = mapper.readValue(response.body(), TwitchResponse.class);
                logger.info("Twitch bot id : " + parsedResponse.user_id
                        + "\nTwitch bot name : " + parsedResponse.login
                        + "\nTwitch bot token expiration time : " + parsedResponse.expires_in +"s");
            } catch (JsonProcessingException e) {
                logger.severe("Unable to read the Twitch validation response.");
                System.exit(1);
            }
        }
        return isValid;
    }

    private boolean isOk(HttpResponse<String> response){
        int statusCode = response.statusCode();
        switch (statusCode) {
            case 200 -> {
                logger.info("Access Granted");
                return true;
            }
            case 401 -> {
                logger.severe("Unauthorized Access");
                return false;
            }
            default -> {
                logger.severe("Token validation error (code" + statusCode + ")");
                return false;
            }
        }
    }
}
