package fr.ollprogram.twitchdiscordbridge.configuration.factory;

import fr.ollprogram.twitchdiscordbridge.configuration.builder.BConfBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.builder.BridgeConfigBuilder;
import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigFileTest {

    private ConfigFromFile fact;

    @BeforeEach
    void before(){
        BridgeConfigBuilder builder = new BConfBuilder();
        fact = new ConfigFromProps(builder);
    }

    @Test
    @Tag("Robustness")
    @DisplayName("missing builder constructor param")
    void nullConstructor1(){
        assertThrows(IllegalArgumentException.class, () -> new ConfigFromProps(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("null resource name")
    void nullLoad(){
        assertThrows(IllegalArgumentException.class, () -> fact.load(null));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("resource not found")
    void notFoundRes(){
        assertThrows(FileNotFoundException.class, () -> fact.load("unknown"));
    }

    @Test
    @Tag("Robustness")
    @DisplayName("default file not found")
    void notFound(){
        assertThrows(FileNotFoundException.class, () -> fact.load());
    }

    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props empty create")
    void createBad1() throws IOException {
        fact.load("/configFactory/empty.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete props empty")
    void incomplete1() throws IOException {
        fact.load("/configFactory/empty.properties");
        assertFalse(fact.isComplete());
    }


    @Test
    @Tag("Robustness")
    @DisplayName("Incomplete props empty create")
    void createBad2() throws IOException {
        fact.load("/configFactory/randomEmpty.properties");
        assertThrows(IncompleteArgumentException.class, () -> fact.createConfiguration());
    }

    @Test
    @DisplayName("Incomplete props empty")
    void incomplete2() throws IOException {
        fact.load("/configFactory/randomEmpty.properties");
        assertFalse(fact.isComplete());
    }
}
