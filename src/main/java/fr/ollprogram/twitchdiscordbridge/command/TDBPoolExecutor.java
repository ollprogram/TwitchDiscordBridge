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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the Command executor, using a ScheduledThreadPoolExecutor.
 */
public class TDBPoolExecutor implements TDBExecutor {
    private final ExecutorService taskPool;

    private final ExecutorService commandPool;

    public TDBPoolExecutor(int commandPoolSize, int taskPoolSize) {
        this.commandPool = Executors.newFixedThreadPool(commandPoolSize);
        this.taskPool = Executors.newFixedThreadPool(taskPoolSize);
    }

    @Override
    public @NotNull CompletableFuture<String> submit(@NotNull Command command, @NotNull List<@NotNull String> args) {
        return CompletableFuture.supplyAsync(command.getExecution(args), commandPool);
    }

    @Override
    public void submit(@NotNull Runnable task) {
        taskPool.submit(task);
    }

    @Override
    public boolean shutdown() throws InterruptedException {
        taskPool.shutdown();
        commandPool.shutdown();
        return commandPool.awaitTermination(5, TimeUnit.SECONDS) && taskPool.awaitTermination(5, TimeUnit.SECONDS);
    }

}
