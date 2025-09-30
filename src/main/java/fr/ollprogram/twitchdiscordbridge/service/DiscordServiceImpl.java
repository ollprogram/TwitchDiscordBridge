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
import fr.ollprogram.twitchdiscordbridge.exception.ServiceDecodeFailedException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceDisconnectedException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceRequestFailedException;
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

/**
 * The discord service implementation
 */
public class DiscordServiceImpl implements DiscordService {

    private static final String BASE_ROUTE = "https://discord.com/api/v10";

    private static final String AUTH_ROUTE = "/oauth2/applications/@me";

    private static final String CHANNELS_ROUTE = "/channels";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final String UNKNOWN_CHANNEL_MESSAGE = "Unknown Channel";

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
    private final HttpClient client;

    /**
     * Constructor where Http client can be specified
     * @param client The http client for the service
     */
    public DiscordServiceImpl(HttpClient client){
        this.client = client;
        this.token = null;
    }

    /**
     * Discord service constructor
     * Using the default HttpClient
     */
    public DiscordServiceImpl(){
       this(HttpClient.newBuilder().build());
    }

    /**
     * Check if the authenticate method was called once
     */
    private void checkAuthCalled(){
        if(token == null) throw new ServiceDisconnectedException("The discord service authentication failed or wasn't called first.");
    }

    @Override
    public @NotNull Optional<DiscordBotInfo> authenticate(String token) throws ServiceException {
        try {
            AuthValidationBody response = callCheckDiscordToken(token);
            if(response != null) {
                this.token = token;
                return Optional.of(new DiscordBotInfo(response.id, response.name));
            }
        } catch (IOException | InterruptedException e ){
            LOG.severe("Unable to request discord, reason : "+e.getMessage());
            throw new ServiceRequestFailedException("Unable to request discord, reason : "+e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<DiscordChannelInfo> getChannel(String channelID) throws ServiceException {
        checkAuthCalled();
        try {
            ChannelBody body = callGetChannelByID(channelID);
            if(body != null) return Optional.of(new DiscordChannelInfo(body.id, body.name));
        } catch (IOException | InterruptedException e) {
            LOG.severe("Unable to request discord, reason : "+e.getMessage());
            throw new ServiceRequestFailedException("Unable to request discord, reason : "+e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Prepare the default discord api headers
     * @param token The discord bot token
     * @return The request builder with default headers
     */
    private HttpRequest.Builder prepareRequestHeaders(String token){
        return HttpRequest.newBuilder().header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"Bot "+token);
    }

    /**
     * Prepare the request for the token validation
     * @param token The discord token
     * @return The request for the token validation
     */
    private HttpRequest getAuthValidationRequest(String token){
        return prepareRequestHeaders(token).GET()
                .uri(URI.create(BASE_ROUTE + AUTH_ROUTE))
                .build();
    }

    /**
     * Prepare the request for retrieving a channel by its ID
     * @param token The discord token
     * @param channelID The discord channel ID
     * @return the request for retrieving a channel by its ID
     */
    private HttpRequest getChannelRequest(String token, String channelID){
        return prepareRequestHeaders(token).GET()
                .uri(URI.create(BASE_ROUTE + CHANNELS_ROUTE + "/" + channelID))
                .build();
    }

    /**
     * Call to the discord API to check if the token is valid
     * @param token The discord token
     * @return The simplified body response or null if invalid
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If an interruption error occurs
     */
    private AuthValidationBody callCheckDiscordToken(String token) throws IOException, InterruptedException, ServiceException {
        HttpRequest discordRequest = getAuthValidationRequest(token);
        HttpResponse<String> response = client.send(discordRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        AuthValidationBody body;
        try {
            body = mapper.readValue(response.body(), AuthValidationBody.class);
        } catch (JsonProcessingException e) {
            LOG.severe("Unable to read the Discord validation response.");
            throw new ServiceDecodeFailedException("Unable to read the Discord validation response.");
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
                throw new ServiceRequestFailedException("Request error : status=" + status + ", message=" + body.message);
            }
        }
    }

    /**
     * Call to the discord API to get a channel by its id
     * @param channelID The id of the channel to retrieve
     * @return The simplified body response or null if not found
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If an interruption error occurs
     */
    private ChannelBody callGetChannelByID(String channelID) throws IOException, InterruptedException, ServiceException {
        HttpRequest request = getChannelRequest(token, channelID);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        ChannelBody body;
        try {
            body = mapper.readValue(response.body(), ChannelBody.class);
        } catch(JsonProcessingException e) {
            LOG.severe("Unable to read the Discord getChannel response.");
            throw new ServiceDecodeFailedException("Unable to read the Discord getChannel response.");
        }
        int status = response.statusCode();
        if(status == 200) return body;
        else if(status == 404 && body.message.equals(UNKNOWN_CHANNEL_MESSAGE)) return null;
        else {
            LOG.severe("Request error : status=" + status + ", message=" + body.message);
            throw new ServiceRequestFailedException("Request error : status=" + status + ", message=" + body.message);
        }
    }

}
