/* Copyright © 2024 ollprogram
 *
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
import fr.ollprogram.twitchdiscordbridge.configuration.build.BConfBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.build.BridgeConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigFromFile;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigFromProps;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfiguratorCLI {

    private final BridgeConfigBuilder builder;
    private BridgeConfig config;

    private static  Logger log = Logger.getLogger("Configurator CLI");

    public ConfiguratorCLI(){
        this.builder = new BConfBuilder();
        this.config = null;
    }

    public Bridge configure(){
        boolean complete = false;
        boolean configured = false;
        try {
            complete = loadFromFile();
        } catch (IOException e) {
            log.severe("Can't load the configuration. Because of files conflicts.");
            System.exit(1);
        }
        if(complete){
            //TODO
        }
        while(!configured){

        }
        return null;//TODO
    }

    private boolean loadFromFile() throws IOException {
        File f = new File(ConfigFromFile.DEFAULT_FILE_NAME+".props");
        if(!f.isFile() || !f.exists()) {
            if(!f.createNewFile()) throw new IOException("Can't create the configuration file");
        }
        ConfigFromFile configLoader = new ConfigFromProps(builder);
        configLoader.load();
        boolean res = configLoader.isComplete();
        if(res) this.config = configLoader.createConfiguration();
        return res;
    }

}
