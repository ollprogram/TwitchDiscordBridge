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
import fr.ollprogram.twitchdiscordbridge.model.TwitchBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * Implementation of the twitch service
 */
public class TwitchServiceImpl implements TwitchService {

    private static final String BASE_AUTH_ROUTE = "https://id.twitch.tv";

    private static final String BASE_API_ROUTE = "https://api.twitch.tv";

    private static final String AUTH_ROUTE = "/oauth2/validate";

    private static final String USERS_ROUTE = "/helix/users";

    private static final String AUTHORIZATION_HEADER = "authorization";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final Logger LOG = LoggerFactory.getLogger("TwitchService");

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AuthBody {
        public String client_id;
        public long expires_in;

        public String message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserBody {
        public String id;
        public String login;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserListBody {
        public UserBody[] data;
        public String message;
    }


    private String token;

    private String clientID;

    private final HttpClient client;

    /**
     * Constructor
     * @param client The http client for the service
     */
    public TwitchServiceImpl(HttpClient client){
        this.client = client;
        this.token = null;
        this.clientID = null;
    }

    /**
     * Constructor using the default Http client
     */
    public TwitchServiceImpl(){
        this(HttpClient.newBuilder().build());
    }


    @Override
    public @NotNull Optional<TwitchBotInfo> authenticate(String token) throws ServiceException {
        try {
            AuthBody body = callValidateToken(token);
            if(body != null) {
                this.clientID = body.client_id;
                this.token = token;
                return Optional.of(new TwitchBotInfo(body.client_id, getExpirationDate(body.expires_in)));
            }
        } catch (IOException | InterruptedException e ){
            LOG.error("Unable to request twitch, reason : "+e.getMessage());
            throw new ServiceRequestFailedException("Unable to request twitch, reason : "+e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Check if authenticate operation was called before
     */
    private void checkAuthCalled(){
        if(token == null || clientID == null) throw new ServiceDisconnectedException("Authentication failed or wasn't called before this");
    }

    @Override
    public @NotNull Optional<TwitchChannelInfo> getChannel(String channelName) throws ServiceException {
        checkAuthCalled();
        try {
            UserListBody userListBody = callGetChannelByName(token, clientID, channelName);
            if(userListBody != null && userListBody.data.length > 0) {
                UserBody userBody = userListBody.data[0];
                return Optional.of(new TwitchChannelInfo(userBody.id, userBody.login));
            }
        }catch (IOException | InterruptedException e ){
            LOG.error("Unable to request twitch, reason : "+e.getMessage());
            throw new ServiceRequestFailedException("Unable to request twitch, reason : "+e.getMessage());
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

    /**
     * Prepare request headers
     * @param token The twitch token
     * @return The RequestBuilder with all common headers for twitch API
     */
    private HttpRequest.Builder prepareHeaders(String token){
        return HttpRequest.newBuilder().header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE)
                .header(AUTHORIZATION_HEADER,"Bearer "+token);
    }

    /**
     * Prepare request headers
     * @param token The twitch token
     * @param clientID The twitch clientID
     * @return The RequestBuilder with all common headers for twitch API
     */
    private HttpRequest.Builder prepareHeadersWithClientID(String token, String clientID){
        return prepareHeaders(token).header("Client-Id", clientID);
    }

    /**
     * Get the twitch auth validation request
     * @param token The twitch token
     * @return The twitch token validation request, ready to be sent
     */
    private HttpRequest getTokenValidateRequest(String token){
        return prepareHeaders(token).GET()
                .uri(URI.create(BASE_AUTH_ROUTE + AUTH_ROUTE))
                .build();
    }

    /**
     * Get the twitch channel search request
     * @param token The twitch token
     * @return The twitch token validation request, ready to be sent
     */
    private HttpRequest getTwitchChannelRequest(String token, String clientID, String channelName){
        return prepareHeadersWithClientID(token, clientID).GET()
                .uri(URI.create(BASE_API_ROUTE + USERS_ROUTE + "?login="+channelName))
                .build();
    }

    /**
     * Call the twitch token validation endpoint
     * @param token The twitch token
     * @return The Auth response body (filtered)
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException If an interruption error occurs
     */
    private AuthBody callValidateToken(String token) throws IOException, InterruptedException, ServiceException {
        HttpRequest twitchRequest = getTokenValidateRequest(token);
        HttpResponse<String> response = client.send(twitchRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        AuthBody body;
        try {
            body = mapper.readValue(response.body(), AuthBody.class);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to read the Twitch validation response.");
            throw new ServiceDecodeFailedException("Unable to read the Twitch validation response.");
        }
        int status = response.statusCode();
        switch (status) {
            case 200 -> {
                if(body.expires_in <= 0){
                    LOG.warn("Twitch token expired");
                    return null;
                }
                return body;
            }
            case 401 -> {
                return null;
            }
            default -> {
                LOG.error("Request error : status=" + status + ", message=" + body.message);
                throw new ServiceRequestFailedException("Request error : status=" + status + ", message=" + body.message);
            }
        }
    }

    /**
     * Call the users endpoint and retrieve the channel/user corresponding to the channelName given
     * @param token The twitch token
     * @param clientID The client ID
     * @param channelName The channel Name
     * @return The users response body (fields filtered)
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException If an interruption error occurs
     */
    private UserListBody callGetChannelByName(String token, String clientID, String channelName) throws IOException, InterruptedException, ServiceException {
        HttpRequest request = getTwitchChannelRequest(token, clientID, channelName);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        UserListBody body;
        try {
            body = mapper.readValue(response.body(), UserListBody.class);
        } catch(JsonProcessingException e){
            LOG.error("Unable to read twitch users response");
            throw new ServiceDecodeFailedException("Unable to read the Twitch validation response.");
        }
        int status = response.statusCode();
        if(status == 200){
            return body;
        }else {
            LOG.error("Request error : status=" + status + ", message=" + body.message);
            throw new ServiceRequestFailedException("Request error : status=" + status + ", message=" + body.message);
        }
    }


}
