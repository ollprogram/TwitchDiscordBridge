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

package fr.ollprogram.twitchdiscordbridge.bridge;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.TwitchChat;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfigImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaverToProps;
import fr.ollprogram.twitchdiscordbridge.exception.BridgeNotOpenedException;
import fr.ollprogram.twitchdiscordbridge.model.TwitchChannelInfo;
import fr.ollprogram.twitchdiscordbridge.service.TwitchServiceImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BridgeTest {

    private Bridge bridge;

    private JDA fakeDiscordBot;


    private TwitchClient fakeTwitchBot;

    private TwitchChat fakeTwitchChat;

    private BridgeConfig fakeConfig;

    private static final String FAKE_DISCORD_TOKEN = "discord_token";

    private static final String FAKE_TWITCH_TOKEN = "twitch_token";

    private static final String FAKE_DISCORD_CHANNEL = "10200303203";
    private static final String FAKE_TWITCH_CHANNEL = "ollprogram";

    @BeforeEach
    void setUp() {
        fakeTwitchBot = mock(TwitchClient.class);
        fakeDiscordBot = mock(JDA.class);
        fakeTwitchChat = mock(TwitchChat.class);
        when(fakeTwitchBot.getChat()).thenReturn(fakeTwitchChat);
        fakeConfig = new BridgeConfigImpl(FAKE_TWITCH_CHANNEL, FAKE_DISCORD_CHANNEL, FAKE_TWITCH_TOKEN, FAKE_DISCORD_TOKEN);// can be mocked but there not too many drawbacks
        bridge = new BridgeImpl(fakeDiscordBot, fakeTwitchBot, fakeConfig);
    }

    @Test
    @DisplayName("Bridge is close by default")
    void isCloseByDefault() {
        assertFalse(bridge.isOpen());
    }

    @Test
    @DisplayName("Bridge is open after open()")
    void isOpenAfterOpen() {
        bridge.open();
        assertTrue(bridge.isOpen());
    }

    @Test
    @DisplayName("Bridge is close after close()")
    void isCloseAfterClose() {
        bridge.open();
        bridge.close();
        assertFalse(bridge.isOpen());
    }

    @Test
    void sendToTwitch() {
        bridge.open();
        bridge.sendToTwitch("message");
        verify(fakeTwitchChat).sendMessage(FAKE_TWITCH_CHANNEL, "message");
    }

    @Test
    void sendToDiscord() {
        bridge.open();
        TextChannel fakeDiscordTextChannel = mock(TextChannel.class);
        when(fakeDiscordBot.getTextChannelById(FAKE_DISCORD_CHANNEL)).thenReturn(fakeDiscordTextChannel);
        when(fakeDiscordTextChannel.sendMessage(any(CharSequence.class))).thenReturn(mock(MessageCreateAction.class));
        bridge.sendToDiscord("message");
        verify(fakeDiscordTextChannel).sendMessage("message");
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Can't send to discord when closed")
    void cantSendToDiscord() {
        bridge.close();
        assertThrows(BridgeNotOpenedException.class, () -> bridge.sendToDiscord("message"));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Can't send to twitch when closed")
    void cantSendToTwitch() {
        bridge.close();
        assertThrows(BridgeNotOpenedException.class, () -> bridge.sendToTwitch("message"));
    }

    @Test
    void changeDiscordChannel() {
        TextChannel fakeDiscordTextChannel = mock(TextChannel.class);
        when(fakeDiscordBot.getTextChannelById("10001")).thenReturn(fakeDiscordTextChannel);
        when(fakeDiscordTextChannel.getName()).thenReturn("ollprogram");
        try(MockedConstruction<ConfigSaverToProps> ignored = mockConstruction(ConfigSaverToProps.class)){
            assertTrue(bridge.changeDiscordChannel("10001") && bridge.getConfig().getDiscordChannelID().equals("10001"));
        }
    }

    @Test
    void changeDiscordChannelNotFound() {
        assertFalse(bridge.changeDiscordChannel("10001"));
    }


    @Test
    void changeTwitchChannel() {
        try (
                MockedConstruction<TwitchServiceImpl> ignored = mockConstruction(TwitchServiceImpl.class, (service, context) -> {
            when(service.getChannel("new_channel")).thenReturn(Optional.of(new TwitchChannelInfo("any", "new_channel")));
        });
                MockedConstruction<ConfigSaverToProps> ignored1 = mockConstruction(ConfigSaverToProps.class))
        {
            assertTrue(bridge.changeTwitchChannel("new_channel") && bridge.getConfig().getTwitchChannelName().equals("new_channel"));
        }
    }

    @Test
    void changeTwitchChannelNotFound() {
        try (MockedConstruction<TwitchServiceImpl> ignored = mockConstruction(TwitchServiceImpl.class, (service, context) -> {
                    when(service.getChannel("new_channel")).thenReturn(Optional.empty());}))
            {
                assertFalse(bridge.changeTwitchChannel("new_channel"));
            }
    }

    @Test
    void isDiscordTarget() {
        assertTrue(bridge.isDiscordTarget(FAKE_DISCORD_CHANNEL));
    }

    @Test
    void isDiscordNotTarget() {
        assertFalse(bridge.isDiscordTarget("wrong channel"));
    }

    @Test
    void isTwitchTarget() {
        assertTrue(bridge.isTwitchTarget(FAKE_TWITCH_CHANNEL));
    }

    @Test
    void isTwitchNotTarget() {
        assertFalse(bridge.isTwitchTarget("wrong channel"));
    }

    @Test
    void isGetConfigCopied() {
        BridgeConfig before = bridge.getConfig();
        BridgeConfig mutation = bridge.getConfig();
        mutation.changeDiscordChannelID("invalid");
        mutation.changeTwitchChannelName("invalid");
        assertEquals(before, bridge.getConfig());
    }
}