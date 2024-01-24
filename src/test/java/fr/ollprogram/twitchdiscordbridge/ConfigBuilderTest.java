package fr.ollprogram.twitchdiscordbridge;

import fr.ollprogram.twitchdiscordbridge.configuration.builder.BConfBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.builder.BridgeConfigBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigBuilderTest {

    private BridgeConfigBuilder b;
    @BeforeEach
    void before(){
        b = new BConfBuilder();
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete empty")
    void incompleteEmpty(){
        assertThrows(IllegalArgumentException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing discord token")
    void incompleteOne1(){
        b.setDiscordChannelID("");
        b.setTwitchToken("");
        b.setTwitchChannelName("");
        assertThrows(IllegalArgumentException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing twitch token")
    void incompleteOne2(){
        b.setDiscordChannelID("");
        b.setTwitchChannelName("");
        b.setDiscordToken("");
        assertThrows(IllegalArgumentException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing discord channel")
    void incompleteOne3(){
        b.setTwitchToken("");
        b.setTwitchChannelName("");
        b.setDiscordToken("");
        assertThrows(IllegalArgumentException.class, () -> b.build());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("incomplete missing twitch channel")
    void incompleteOne4(){
        b.setDiscordChannelID("");
        b.setTwitchToken("");
        b.setDiscordToken("");
        assertThrows(IllegalArgumentException.class, () -> b.build());
    }

    @Test
    @DisplayName("complete")
    void complete(){
        b.setDiscordChannelID("");
        b.setTwitchToken("");
        b.setDiscordToken("");
        b.setTwitchChannelName("");
        assertTrue(b.isComplete());
    }

    @Test
    @DisplayName("incomplete empty")
    void incomplete(){
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing twitch channel")
    void incompleteOneTC(){
        b.setDiscordChannelID("");
        b.setTwitchToken("");
        b.setDiscordToken("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing discord channel")
    void incompleteOneDC(){
        b.setTwitchToken("");
        b.setDiscordToken("");
        b.setTwitchChannelName("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing twitch token")
    void incompleteOneTT(){
        b.setDiscordChannelID("");
        b.setDiscordToken("");
        b.setTwitchChannelName("");
        assertFalse(b.isComplete());
    }

    @Test
    @DisplayName("incomplete missing discord token")
    void incompleteOneDT(){
        b.setTwitchToken("");
        b.setDiscordChannelID("");
        b.setTwitchChannelName("");
        assertFalse(b.isComplete());
    }
}
