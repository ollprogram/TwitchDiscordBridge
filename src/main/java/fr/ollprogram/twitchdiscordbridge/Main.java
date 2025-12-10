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

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import fr.ollprogram.twitchdiscordbridge.bridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.bridge.BridgeImpl;
import fr.ollprogram.twitchdiscordbridge.cli.BridgeCLI;
import fr.ollprogram.twitchdiscordbridge.cli.ConfiguratorCLI;
import fr.ollprogram.twitchdiscordbridge.command.*;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.factory.BotFactory;
import fr.ollprogram.twitchdiscordbridge.factory.BotFactoryImpl;
import fr.ollprogram.twitchdiscordbridge.listener.DiscordListener;
import fr.ollprogram.twitchdiscordbridge.listener.TwitchListener;
import fr.ollprogram.twitchdiscordbridge.manager.AppsManager;
import fr.ollprogram.twitchdiscordbridge.manager.AppsManagerImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static void main(String[] args) {
        LOG.info(LICENCE);
        int threads = Runtime.getRuntime().availableProcessors();
        int commandPoolSize = (int) (threads * 0.2); // only 20% ~= 2 threads, this is not the most demanded task
        int taskPoolSize = (threads - commandPoolSize) * (1 + (500 / 150)); // IDLE time over real calculation time (this is an approximation in ms)
        // maybe this approximation is useless since the twitch api rate limit is a bottleneck.
        LOG.info("Using "+commandPoolSize+" threads for commands and "+taskPoolSize+" threads for TDB messages and TDB tasks");
        TDBExecutor executor = new TDBPoolExecutor(commandPoolSize, taskPoolSize);
        Scanner scanner = new Scanner(System.in);
        ConfiguratorCLI configuratorCLI = new ConfiguratorCLI(scanner);
        BridgeConfig config = configuratorCLI.configure();

        LOG.info("Starting bots");
        BotFactory botFactory = new BotFactoryImpl(config);
        JDA discordBot = botFactory.createDiscordBot();
        TwitchClient twitchBot = botFactory.createTwitchBot();
        Bridge bridge = new BridgeImpl(discordBot, twitchBot, config);
        AppsManager appsManager = new AppsManagerImpl(executor, discordBot, twitchBot);

        LOG.info("Registering commands");
        CommandRegistry registry = new CommandRegistryImpl();
        registry.register("code", new Code());
        registry.register("say", new Say(bridge));
        registry.register("bridge", "info", new BridgeInfo(bridge, discordBot));
        registry.register("bridge", "open", new BridgeOpen(bridge));
        registry.register("bridge", "close", new BridgeClose(bridge));
        registry.register("bridge", "discord_target", new BridgeDiscordTarget(bridge));
        registry.register("bridge", "twitch_target", new BridgeTwitchTarget(bridge));
        registry.setDiscordPermissions("bridge", DefaultMemberPermissions.DISABLED);
        registry.setDiscordPermissions("say", DefaultMemberPermissions.DISABLED);

        LOG.info("Registering listeners");
        discordBot.addEventListener(new DiscordListener(bridge, registry, executor));
        twitchBot.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchListener(bridge, executor));

        LOG.info("Refreshing discord commands");
        appsManager.refreshDiscordCommands(registry);

        LOG.info(SPLASH + "\nStarted Bridge CLI");
        BridgeCLI bridgeCLI = new BridgeCLI(scanner, registry, appsManager);
        bridgeCLI.run();
        scanner.close();

    }
}
