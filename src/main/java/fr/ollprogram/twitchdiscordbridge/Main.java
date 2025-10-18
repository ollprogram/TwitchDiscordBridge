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

import fr.ollprogram.twitchdiscordbridge.command.*;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.factory.BridgeFactory;
import fr.ollprogram.twitchdiscordbridge.factory.BridgeFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String LICENCE = """
            
            Copyright © 2025 ollprogram
            TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
            as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
            TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
            without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
            See the GNU General Public License for more details.
            You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
            If not, see https://www.gnu.org/licenses.
            
            """;

    private static final String SPLASH = """
            
              ______         _ __       __        \s
             /_  __/      __(_) /______/ /_       \s
              / / | | /| / / / __/ ___/ __ \\      \s
             / /  | |/ |/ / / /_/ /__/ / / /      \s
            /_/ __|__/|__/_/\\__/\\___/_/ /_/      __
               / __ \\(_)_____________  _________/ /
              / / / / / ___/ ___/ __ \\/ ___/ __  /\s
             / /_/ / (__  ) /__/ /_/ / /  / /_/ / \s
            /_____/_/____/\\___/\\____/_/   \\__,_/  \s
               / __ )_____(_)___/ /___ ____       \s
              / __  / ___/ / __  / __ `/ _ \\      \s
             / /_/ / /  / / /_/ / /_/ /  __/      \s
            /_____/_/  /_/\\__,_/\\__, /\\___/       \s
                               /____/             \s
            """;
    private static final Logger LOG = LoggerFactory.getLogger("Main");
    public static void main(String[] args) throws IOException {
        LOG.info(LICENCE);
        Scanner scanner = new Scanner(System.in);
        ConfiguratorCLI configuratorCLI = new ConfiguratorCLI(scanner);

        BridgeConfig config = configuratorCLI.configure();
        CommandRegistry registry = new CommandRegistryImpl();
        CommandExecutor executor = new CommandPoolExecutor(10);
        registry.register("code", new Code());
        BridgeFactory bridgeFactory = new BridgeFactoryImpl(config, registry, executor);
        Bridge bridge = bridgeFactory.createBridge();
        LOG.info(SPLASH + "\nStarted Bridge CLI");
        try {
            bridge.awaitShutdown();
        } catch (InterruptedException e) {
            LOG.error("The bridge exited suddenly : "+e.getMessage());
        }
        scanner.close();

    }
}
