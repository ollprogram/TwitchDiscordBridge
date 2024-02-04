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
package fr.ollprogram.twitchdiscordbridge.configuration.factory;

import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import org.apache.commons.lang.IncompleteArgumentException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A factory/loader which can create a configuration after loading a file which contains the configuration.
 * @author ollprogram
 */
public interface ConfigFromFile {

    String DEFAULT_FILE_NAME = "bridge";

    /**
     * Load the configuration from default file
     * @throws IOException Unable to find or parse the file
     */
    void load() throws IOException;

    /**
     * Load from a specific file name
     * @param pathname The file to load
     * @throws IOException Unable to find or parse the file
     */
    void load(@NotNull String pathname) throws IOException;

    /**
     * Tell if the configuration is complete
     * @return If the configuration is complete
     */
    boolean isComplete();

    /**
     * Create the bridge configuration from the loaded file
     * @return The bridge configuration
     * @throws IncompleteArgumentException If the configuration file was incomplete
     */
    @NotNull BridgeConfig createConfiguration() throws IncompleteArgumentException;
}
