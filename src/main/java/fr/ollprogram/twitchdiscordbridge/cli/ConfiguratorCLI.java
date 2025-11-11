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

package fr.ollprogram.twitchdiscordbridge.cli;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigLoader;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigLoaderFromProps;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaver;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaverToProps;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidator;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Help the user to fill the configuration in the console if elements are missing
 */
public class ConfiguratorCLI {

    private final ConfigBuilder builder;
    private final Scanner scanner;

    private final static Logger LOG = LoggerFactory.getLogger("Configurator CLI");

    /**
     * Configurator constructor
     * @param scanner The file scanner
     */
    public ConfiguratorCLI(Scanner scanner){
        this.builder = new ConfigBuilderImpl();
        this.scanner = scanner;
    }

    /**
     * Ask the user to configure until the configuration is complete and valid
     * @return The validated bridge config
     */
    public BridgeConfig configure(){
        try {
            ConfigLoader configLoader = findOrCreateConfigFile();
            configLoader.load(); //Using default path
        } catch (IOException e) {
            LOG.error("Can't load the configuration. Because of files conflicts.");
            System.exit(1);
        }
        boolean configured = false;
        if(builder.isComplete()) {
            ConfigValidator validator = new ConfigValidatorImpl(builder);
            configured = validator.isValid();
        }
        while(!configured) {
            askDiscordToken();
            askDiscordChannelID();
            askTwitchToken();
            askTwitchChannelName();
            ConfigValidator validator = new ConfigValidatorImpl(builder);
            configured = builder.isComplete() && validator.isValid();
        }
        BridgeConfig config = builder.build();
        saveCurrentConfiguration(config);
        return config;
    }

    /**
     * Ask the twitch token to the user
     */
    private void askTwitchToken(){
        System.out.println("Please provide a twitch Token : ");
        String token = scanner.nextLine().strip();
        builder.setTwitchToken(token);
    }

    /**
     * Ask the discord token to the user
     */
    private void askDiscordToken(){
        System.out.println("Please provide a discord Token : ");
        String token = scanner.nextLine().strip();
        builder.setDiscordToken(token);
    }

    /**
     * Ask the discord channel ID to the User
     */
    private void askDiscordChannelID(){
        System.out.println("Please provide a discord channel ID : ");
        String id = scanner.nextLine().strip();
        builder.setDiscordChannelID(id);
    }

    /**
     * Ask the twitch channel name to the user
     */
    private void askTwitchChannelName(){
        System.out.println("Please provide a twitch channel name : ");
        String name = scanner.nextLine().strip();
        builder.setTwitchChannelName(name);
    }

    /**
     * Find or create the configuration file
     * @return The configuration loader
     * @throws IOException If an I/O error occurs
     */
    private ConfigLoader findOrCreateConfigFile() throws IOException {
        File f = new File(ConfigLoader.DEFAULT_FILE_NAME+".properties");
        if(!f.isFile() || !f.exists()) {
            if(!f.createNewFile()) throw new IOException("Can't create the configuration file");
        }
        return new ConfigLoaderFromProps(builder);
    }

    /**
     * Save the current configuration
     * @param config The bridge configuration
     */
    private void saveCurrentConfiguration(BridgeConfig config) {
        ConfigSaver saver = new ConfigSaverToProps();
        try {
            saver.saveConfiguration(config);
        } catch (IOException e) {
            LOG.warn("Unable to save configuration");
        }
    }

}
