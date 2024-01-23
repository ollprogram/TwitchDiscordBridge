package fr.ollprogram.twitchdiscordbridge;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.factory.ConfigPropertiesMaker;
import fr.ollprogram.twitchdiscordbridge.configuration.factory.ConfigurationFactory;
import org.junit.jupiter.api.BeforeEach;

public class ConfigurationTest {

    private BridgeConfig config;

    @BeforeEach
    void beforeEach(){
        ConfigurationFactory fact = new ConfigPropertiesMaker("test");
        config = fact.createConfiguration();
    }
}
