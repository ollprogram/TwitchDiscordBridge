package fr.ollprogram.twitchdiscordbridge.configuration.saving;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

public class ConfigToProps implements ConfigToFile{

    private static final String PROPERTIES_FILE = "bridge.properties";
    @Override
    public void saveConfiguration(@NotNull BridgeConfig bridgeConfig) throws IOException {
        Writer w = new FileWriter(PROPERTIES_FILE);
        Properties props = new Properties();
        props.put("TwitchToken", bridgeConfig.getTwitchToken());
        props.put("DiscordToken", bridgeConfig.getDiscordToken());
        props.put("TwitchChannelName", bridgeConfig.getTwitchChannelName());
        props.put("DiscordChannelID", bridgeConfig.getDiscordChannelID());
        props.store(w, "You can edit this file if you wish");
    }//TODO tests
}
