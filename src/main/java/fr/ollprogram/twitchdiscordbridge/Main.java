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
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilder;
import fr.ollprogram.twitchdiscordbridge.configuration.build.ConfigBuilderImpl;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigFromFile;
import fr.ollprogram.twitchdiscordbridge.configuration.load.ConfigFromProps;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidator;
import fr.ollprogram.twitchdiscordbridge.configuration.validate.ConfigValidatorImpl;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {

    private static final String LICENCE = """
            
            Copyright © 2024 ollprogram
            TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
            as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
            TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
            without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
            See the GNU General Public License for more details.
            You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
            If not, see https://www.gnu.org/licenses.
            """;

    public static void main(String[] args) throws IOException {
        Logger lg = Logger.getLogger("Main");
        lg.info(LICENCE);
        lg.info("Started Bridge CLI");
        ConfigBuilderImpl configBuilder = new ConfigBuilderImpl();
        ConfigFromFile configFactory = new ConfigFromProps(configBuilder);
        configFactory.load();
        BridgeConfig config = configFactory.createConfiguration();
        ConfigValidator validator = new ConfigValidatorImpl(config);
        lg.info("is valid = " + validator.isValid());
    }
}
