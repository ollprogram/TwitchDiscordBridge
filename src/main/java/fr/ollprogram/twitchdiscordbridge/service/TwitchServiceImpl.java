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

package fr.ollprogram.twitchdiscordbridge.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ollprogram.twitchdiscordbridge.model.DiscordBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchBotInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

public class TwitchServiceImpl implements TwitchService {

    private static final String TWITCH_BASE_ROUTE= "https://id.twitch.tv";

    private static final String TWITCH_AUTH_ROUTE = "/oauth2/validate";

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final Logger LOG = Logger.getLogger("TwitchService");

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TwitchAuthBody {
        public String client_id;
        public long expires_in;

        public String message;
    }


    @Override
    public @NotNull Optional<TwitchBotInfo> authenticate(String token) {
        try {
            TwitchAuthBody body = checkTwitchToken(token);
            if(body != null) return Optional.of(new TwitchBotInfo(body.client_id, getExpirationDate(body.expires_in)));
        } catch (IOException | InterruptedException e ){
            LOG.severe("Unable to request twitch, reason : "+e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Get the expiration date with the token expiration time
     * @param expiresIn The time before the token expiration
     * @return The expiration date
     */
    private static Date getExpirationDate(long expiresIn){
        return Date.from(Instant.now().plusSeconds(expiresIn));
    }

    private HttpRequest getTwitchAuthRequest(String token){
        return HttpRequest.newBuilder().GET()
                .uri(URI.create(TWITCH_BASE_ROUTE + TWITCH_AUTH_ROUTE))
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"OAuth "+token)
                .build();
    }

    private TwitchAuthBody checkTwitchToken(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest twitchRequest = getTwitchAuthRequest(token);
        HttpResponse<String> response = client.send(twitchRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        TwitchAuthBody body = null;
        try {
            body = mapper.readValue(response.body(), TwitchAuthBody.class);
        } catch (JsonProcessingException e) {
            LOG.severe("Unable to read the Twitch validation response.");
            System.exit(1);
        }
        int status = response.statusCode();
        switch (status) {
            case 200 -> {
                if(body.expires_in <= 0){
                    LOG.warning("Twitch token expired");
                    return null;
                }
                return body;
            }
            case 401 -> {
                return null;
            }
            default -> {
                LOG.severe("Request error : status=" + status + ", message=" + body.message);
                System.exit(1);
            }
        }
        return null;
    }
}
