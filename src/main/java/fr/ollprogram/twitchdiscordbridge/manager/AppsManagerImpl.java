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

package fr.ollprogram.twitchdiscordbridge.manager;

import com.github.twitch4j.TwitchClient;
import fr.ollprogram.twitchdiscordbridge.command.CommandRegistry;
import fr.ollprogram.twitchdiscordbridge.command.TDBExecutor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * AppsManager implementation
 */
public class AppsManagerImpl implements AppsManager {

    private final TDBExecutor executor;

    private final JDA discordApp;

    private final TwitchClient twitchApp;

    private boolean shutdown;

    private static final Logger LOG = LoggerFactory.getLogger("AppsManager");

    public AppsManagerImpl(TDBExecutor executorApp, JDA discordApp, TwitchClient twitchApp){
        this.discordApp = discordApp;
        this.twitchApp = twitchApp;
        this.executor = executorApp;
        this.shutdown = false;
    }

    @Override
    public void shutdownAll() throws InterruptedException {
        LOG.info("Shutdown remaining tasks...");
        if(!executor.shutdown()){
            LOG.warn("Forced executor shutdown due to timeout.");
        }
        LOG.info("Shutdown discord bot...");
        discordApp.shutdown();
        discordApp.awaitShutdown();
        LOG.info("Shutdown twitch bot...");
        twitchApp.close();
        shutdown = true;
    }

    @Override
    public void shutdownAllNow() throws InterruptedException {
        LOG.info("Shutdown remaining tasks...");
        if(!executor.shutdown()){
            LOG.warn("Forced executor shutdown due to timeout.");
        }
        LOG.info("Shutdown discord bot...");
        discordApp.shutdownNow();
        discordApp.awaitShutdown();
        LOG.info("Shutdown twitch bot...");
        twitchApp.close();
        shutdown = true;
    }

    @Override
    public boolean areAllRunning() {
        return !shutdown;
    }

    @Override
    public void refreshDiscordCommands(@NotNull CommandRegistry registry) {
        discordApp.getGuilds().parallelStream().forEach((guild) -> {
            List<Command> commands = guild.retrieveCommands().complete();
            commands.parallelStream().forEach(command -> command.delete().complete());
            guild.updateCommands().addCommands(registry.getAllDiscordCommands()).complete();
        });
    }

    @Override
    public @NotNull TDBExecutor getExecutor() {
        return executor;
    }
}
