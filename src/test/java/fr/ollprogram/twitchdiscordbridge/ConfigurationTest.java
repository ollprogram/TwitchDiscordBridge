package fr.ollprogram.twitchdiscordbridge;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.builder.BConfBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.factory.ConfigFromProps;
import fr.ollprogram.twitchdiscordbridge.configuration.factory.ConfigFromFile;
import org.junit.jupiter.api.BeforeEach;

public class ConfigurationTest {

    private BridgeConfig config;

    @BeforeEach
    void beforeEach(){
        ConfigFromFile fact = new ConfigFromProps("test", new BConfBuilder());
        config = fact.createConfiguration();
    }
}
