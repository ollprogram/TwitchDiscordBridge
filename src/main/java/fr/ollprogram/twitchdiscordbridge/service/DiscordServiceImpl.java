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
import fr.ollprogram.twitchdiscordbridge.exception.ServiceDisconnectedException;
import fr.ollprogram.twitchdiscordbridge.model.DiscordBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.DiscordChannelInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.logging.Logger;

public class DiscordServiceImpl implements DiscordService {

    private static final String BASE_ROUTE = "https://discord.com/api/v10";

    private static final String AUTH_ROUTE = "/oauth2/applications/@me";

    private static final String CHANNELS_ROUTE = "/channels";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final Logger LOG = Logger.getLogger("DiscordService");

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AuthValidationBody {
        public String id;

        public String name;

        public String message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ChannelBody {
        public String id;

        public String name;

        public String message;
    }

    private String token;

    public DiscordServiceImpl(){
        this.token = null;
    }

    private void checkAuthCalled(){
        if(token == null) throw new ServiceDisconnectedException("The discord service authentication failed or wasn't called first.");
    }

    @Override
    public @NotNull Optional<DiscordBotInfo> authenticate(String token) {
        try {
            AuthValidationBody response = callCheckDiscordToken(token);
            if(response != null) {
                this.token = token;
                return Optional.of(new DiscordBotInfo(response.id, response.name));
            }
        } catch (IOException | InterruptedException e ){
            LOG.severe("Unable to request discord, reason : "+e.getMessage());
            System.exit(1);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<DiscordChannelInfo> getChannel(String channelID) {
        checkAuthCalled();
        try {
            ChannelBody body = callGetChannelByID(channelID);
            if(body != null) return Optional.of(new DiscordChannelInfo(body.id, body.name));
        } catch (IOException | InterruptedException e) {
            LOG.severe("Unable to request discord, reason : "+e.getMessage());
            System.exit(1);
        }
        return Optional.empty();
    }

    private HttpRequest.Builder prepareRequestHeaders(String token){
        return HttpRequest.newBuilder().header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"Bot "+token);
    }

    private HttpRequest getAuthValidationRequest(String token){
        return prepareRequestHeaders(token).GET()
                .uri(URI.create(BASE_ROUTE + AUTH_ROUTE))
                .build();
    }

    private HttpRequest getChannelRequest(String token, String channelID){
        return prepareRequestHeaders(token).GET()
                .uri(URI.create(BASE_ROUTE + CHANNELS_ROUTE + "/" + channelID))
                .build();
    }

    private AuthValidationBody callCheckDiscordToken(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest discordRequest = getAuthValidationRequest(token);
        HttpResponse<String> response = client.send(discordRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        AuthValidationBody body = null;
        try {
            body = mapper.readValue(response.body(), AuthValidationBody.class);
        } catch (JsonProcessingException e) {
            LOG.severe("Unable to read the Discord validation response.");
            System.exit(1);
        }
        int status = response.statusCode();
        switch(status){
            case 200 -> {
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

    private ChannelBody callGetChannelByID(String channelID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = getChannelRequest(token, channelID);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        ChannelBody body = null;
        try {
            body = mapper.readValue(response.body(), ChannelBody.class);
        } catch(JsonProcessingException e) {
            LOG.severe("Unable to read the Discord getChannel response.");
            System.exit(1);
        }
        int status = response.statusCode();
        if(status == 200){
            return body;
        } else {
            LOG.severe("Request error : status=" + status + ", message=" + body.message);
            System.exit(1);
        }
        return body;
    }

}
