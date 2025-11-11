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

package fr.ollprogram.twitchdiscordbridge.command;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the Command executor, using a ScheduledThreadPoolExecutor.
 */
public class CommandPoolExecutor implements CommandExecutor {
    private final ExecutorService executorService;

    public CommandPoolExecutor(int poolSize) {
        this.executorService = new ScheduledThreadPoolExecutor(poolSize);
    }

    @Override
    public @NotNull CompletableFuture<String> submit(Command command, List<String> args) {
        return CompletableFuture.supplyAsync(command.getExecution(args), executorService);
    }

    @Override
    public boolean shutdown() throws InterruptedException {
        executorService.shutdown();
        return executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

}
