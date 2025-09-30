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

import fr.ollprogram.twitchdiscordbridge.exception.ServiceDecodeFailedException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceDisconnectedException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceException;
import fr.ollprogram.twitchdiscordbridge.exception.ServiceRequestFailedException;
import fr.ollprogram.twitchdiscordbridge.model.DiscordBotInfo;
import fr.ollprogram.twitchdiscordbridge.model.DiscordChannelInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DiscordServiceTest {

    private DiscordService service;
    private HttpResponse<String> response;

    private static final String ANY_TOKEN = "any_token";
    private static final String SUCCESS_AUTH_BODY = "{\"id\": \"23\", \"name\":\"TDB\"}";
    private static final String ERROR_BODY = "{\"message\": \"error message\"}";

    private static final String NOT_FOUND_CHANNEL_MESSAGE = "{\"message\" : \"Unknown Channel\"}";

    private static final String SUCCESS_CHANNEL_BODY = "{\"id\" : \"23\", \"name\":\"bridgeChannel\"}";

    private static final String MALFORMED_BODY = "{\"id\": \"23\"";

    @BeforeEach
    void setUp() {
        HttpClient client = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);
        try {
            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        service = new DiscordServiceImpl(client);
        this.response = response;
    }

    @Test
    @DisplayName("if authentication is unauthorized the result must be empty")
    void unauthorizedMustBeEmpty() throws ServiceException {
        when(response.body()).thenReturn("{}");
        when(response.statusCode()).thenReturn(401);
        assertEquals(Optional.empty(), service.authenticate(ANY_TOKEN));
    }

    @Test
    @DisplayName("if authentication is unauthorized the result must be empty")
    void authorized() throws ServiceException {
        when(response.body()).thenReturn(SUCCESS_AUTH_BODY);
        when(response.statusCode()).thenReturn(200);
        assertEquals(new DiscordBotInfo("23", "TDB"), service.authenticate(ANY_TOKEN).orElse(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("auth request error 404")
    void authRequestError404() {
        when(response.body()).thenReturn(ERROR_BODY);
        when(response.statusCode()).thenReturn(404);
        assertThrows(ServiceRequestFailedException.class, () -> {service.authenticate(ANY_TOKEN);});
    }

    @Test
    @Tag("Robustness")
    @DisplayName("auth request error 500")
    void authRequestError500() {
        when(response.body()).thenReturn(ERROR_BODY);
        when(response.statusCode()).thenReturn(500);
        assertThrows(ServiceRequestFailedException.class, () -> {service.authenticate(ANY_TOKEN);});
    }

    @Test
    @Tag("Robustness")
    @DisplayName("malformed body must throw")
    void authRequestMalformedBody() {
        when(response.body()).thenReturn(MALFORMED_BODY);
        when(response.statusCode()).thenReturn(200);
        assertThrows(ServiceDecodeFailedException.class, () -> {service.authenticate(ANY_TOKEN);});
    }

    @Test
    @Tag("Robustness")
    @DisplayName("The auth must be called before")
    void getChannelWithoutCallingAuth() {
        assertThrows(ServiceDisconnectedException.class, () -> {service.getChannel("any");});
    }

    /**
     * Mock authentication
     */
    private void mockAuthenticate() throws ServiceException {
        when(response.body()).thenReturn(SUCCESS_AUTH_BODY);
        when(response.statusCode()).thenReturn(200);
        service.authenticate(ANY_TOKEN);
    }

    @Test
    @DisplayName("empty channel if not found")
    void getChannelEmpty() throws ServiceException {
        mockAuthenticate();
        when(response.body()).thenReturn(NOT_FOUND_CHANNEL_MESSAGE);
        when(response.statusCode()).thenReturn(404);
        assertEquals(Optional.empty(), service.getChannel("1232"));
    }

    @Test
    @DisplayName("channel found")
    void getChannel() throws ServiceException {
        mockAuthenticate();
        when(response.body()).thenReturn(SUCCESS_CHANNEL_BODY);
        when(response.statusCode()).thenReturn(200);
        assertEquals(new DiscordChannelInfo("23", "bridgeChannel"), service.getChannel("23").orElse(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("request error 404 must throw")
    void getChannelRequestError404() throws ServiceException {
        mockAuthenticate();
        when(response.body()).thenReturn(ERROR_BODY);
        when(response.statusCode()).thenReturn(404);
        assertThrows(ServiceRequestFailedException.class, () -> {service.getChannel("any");});
    }

    @Test
    @Tag("Robustness")
    @DisplayName("request error 500 must throw")
    void getChannelRequestError500() throws ServiceException {
        mockAuthenticate();
        when(response.body()).thenReturn(ERROR_BODY);
        when(response.statusCode()).thenReturn(500);
        assertThrows(ServiceRequestFailedException.class, () -> {service.getChannel("any");});
    }

    @Test
    @Tag("Robustness")
    @DisplayName("malformed body must throw")
    void getChannelMalformedBody() throws ServiceException {
        mockAuthenticate();
        when(response.body()).thenReturn(MALFORMED_BODY);
        when(response.statusCode()).thenReturn(200);
        assertThrows(ServiceDecodeFailedException.class, () -> {service.getChannel("any");});
    }
}