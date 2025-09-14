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

import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigLoader;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigLoaderFromProps;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidator;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidatorImpl;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfiguratorCLI {

    private final ConfigBuilder builder;

    private static  Logger log = Logger.getLogger("Configurator CLI");

    public ConfiguratorCLI(){
        this.builder = new ConfigBuilderImpl();
    }

    public Bridge configure(){
        boolean configured = false;
        try {
            ConfigLoader configLoader = findOrCreateConfigFile();
            configLoader.load(); //Using default path
            //TODO
        } catch (IOException e) {
            log.severe("Can't load the configuration. Because of files conflicts.");
            System.exit(1);
        }
        while(!configured){
            ConfigValidator validator = new ConfigValidatorImpl(builder);
            configured = validator.isValid() && builder.isComplete();
            //TODO
        }
        return null;//TODO
    }

    private ConfigLoader findOrCreateConfigFile() throws IOException {
        File f = new File(ConfigLoader.DEFAULT_FILE_NAME+".props");
        if(!f.isFile() || !f.exists()) {
            if(!f.createNewFile()) throw new IOException("Can't create the configuration file");
        }
        return new ConfigLoaderFromProps(builder);
    }

}
