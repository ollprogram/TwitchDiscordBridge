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

package fr.ollprogram.twitchdiscordbridge;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigLoader;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigLoaderFromProps;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaver;
import fr.ollprogram.twitchdiscordbridge.configuration.save.ConfigSaverToProps;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidator;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidatorImpl;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConfiguratorCLI {

    private final ConfigBuilder builder;
    private final Scanner scanner;

    private final static Logger LOG = Logger.getLogger("Configurator CLI");

    public ConfiguratorCLI(Scanner scanner){
        this.builder = new ConfigBuilderImpl();
        this.scanner = scanner;
    }

    public BridgeConfig configure(){
        try {
            ConfigLoader configLoader = findOrCreateConfigFile();
            configLoader.load(); //Using default path
        } catch (IOException e) {
            LOG.severe("Can't load the configuration. Because of files conflicts.");
            System.exit(1);
        }
        ConfigValidator validator = new ConfigValidatorImpl(builder);
        boolean configured = builder.isComplete() && validator.isValid();
        while(!configured) {
            validator = new ConfigValidatorImpl(builder);
            askDiscordToken();
            askDiscordChannelID();
            askTwitchToken();
            askTwitchChannelName();
            configured = builder.isComplete() && validator.isValid();
        }
        BridgeConfig config = builder.build();
        saveCurrentConfiguration(config);
        return config;
    }

    private void askTwitchToken(){
        System.out.println("Please provide a twitch Token : ");
        String token = scanner.next();
        builder.setTwitchToken(token);
    }

    private void askDiscordToken(){
        System.out.println("Please provide a discord Token : ");
        String token = scanner.next();
        builder.setDiscordToken(token);
    }

    private void askDiscordChannelID(){
        System.out.println("Please provide a discord channel ID : ");
        String id = scanner.next();
        builder.setDiscordChannelID(id);
    }

    private void askTwitchChannelName(){
        System.out.println("Please provide a twitch channel name : ");
        String name = scanner.next();
        builder.setTwitchChannelName(name);
    }
    private ConfigLoader findOrCreateConfigFile() throws IOException {
        File f = new File(ConfigLoader.DEFAULT_FILE_NAME+".properties");
        if(!f.isFile() || !f.exists()) {
            if(!f.createNewFile()) throw new IOException("Can't create the configuration file");
        }
        return new ConfigLoaderFromProps(builder);
    }

    private void saveCurrentConfiguration(BridgeConfig config) {
        ConfigSaver saver = new ConfigSaverToProps();
        try {
            saver.saveConfiguration(config);
        } catch (IOException e) {
            LOG.fine("Unable to save configuration");
        }
    }

}
