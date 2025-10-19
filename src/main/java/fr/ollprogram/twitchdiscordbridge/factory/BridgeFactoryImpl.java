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

package fr.ollprogram.twitchdiscordbridge.factory;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.ollprogram.twitchdiscordbridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.BridgeImpl;
import fr.ollprogram.twitchdiscordbridge.command.CommandExecutor;
import fr.ollprogram.twitchdiscordbridge.command.CommandRegistry;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import fr.ollprogram.twitchdiscordbridge.listener.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Implementation of a bridge factory.
 * @author ollprogram
 */
public class BridgeFactoryImpl implements BridgeFactory {

    private final BridgeConfig conf;
    private final CommandExecutor executor;
    private final CommandRegistry registry;

    private static final Logger LOG = LoggerFactory.getLogger("BridgeFactory");


    public BridgeFactoryImpl(BridgeConfig conf, CommandRegistry registry, CommandExecutor executor) {
        this.conf = conf;
        this.registry = registry;
        this.executor = executor;
    }

    @Override
    public @NotNull Bridge createBridge() {
        JDA jda = createJDA();
        Bridge bridge = new BridgeImpl(jda, createTwitchClient(), conf);
        jda.addEventListener(new DiscordListener(bridge, registry, executor));
        return bridge;
    }

    /**
     * Create and configure the JDA instance
     * @return The JDA instance
     */
    private @NotNull JDA createJDA() {
        JDA jda = JDABuilder.createDefault(conf.getDiscordToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT).build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            LOG.error("Something went wrong while waiting JDA connection : "+e.getMessage());
            System.exit(1);
        }
        LOG.info("Refreshing discord commands");
        jda.getGuilds().forEach((guild) -> {
            List<Command> commands = guild.retrieveCommands().complete();
            commands.forEach(command -> command.delete().complete());
            guild.updateCommands().addCommands(registry.getAllDiscordCommands()).complete();
        });
        return jda;
    }

    /**
     * Create and configure the twitch client
     * @return The twitch client
     */
    private @NotNull TwitchClient createTwitchClient() {
        OAuth2Credential twitchCred = new OAuth2Credential("twitch", conf.getTwitchToken());
        TwitchClientBuilder builder = TwitchClientBuilder.builder().withChatAccount(twitchCred);
        return builder.build();
    }
}
