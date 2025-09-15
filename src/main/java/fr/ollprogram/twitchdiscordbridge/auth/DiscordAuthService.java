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

package fr.ollprogram.twitchdiscordbridge.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.logging.Logger;

public class DiscordAuthService implements BotAuthService {

    private static final String DISCORD_BASE_ROUTE = "https://discord.com/api/v10";

    private static final String DISCORD_AUTH_ROUTE = "/oauth2/applications/@me";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final Logger LOG = Logger.getLogger("DiscordAuthService");

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DiscordResponse{
        public String id;
        public String name;
    }

    @Override
    public @NotNull Optional<BotInfo> authenticate(String token) {
        try {
            DiscordResponse response = checkDiscordToken(token);
            if(response != null) return Optional.of(new BotInfo(response.id, response.name));
        } catch (IOException | InterruptedException e ){
            LOG.severe("Unable to request discord, reason : "+e.getMessage());
        }
        return Optional.empty();
    }

    private HttpRequest getDiscordRequest(String token){
        return HttpRequest.newBuilder().GET()
                .uri(URI.create(DISCORD_BASE_ROUTE + DISCORD_AUTH_ROUTE))
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"Bot "+token)
                .build();
    }

    private DiscordResponse checkDiscordToken(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest discordRequest = getDiscordRequest(token);
        HttpResponse<String> response = client.send(discordRequest, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(response.body(), DiscordResponse.class);
            } catch (JsonProcessingException e) {
                LOG.severe("Unable to read the Discord validation response.");
                System.exit(1);
            }
        }
        return null;
    }


}
