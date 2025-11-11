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

package fr.ollprogram.twitchdiscordbridge.listener;

import fr.ollprogram.twitchdiscordbridge.bridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.command.Command;
import fr.ollprogram.twitchdiscordbridge.command.CommandExecutor;
import fr.ollprogram.twitchdiscordbridge.command.CommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DiscordListener extends ListenerAdapter  {

    private final Bridge bridge;

    private final CommandRegistry commandRegistry;

    private final CommandExecutor executor;

    public DiscordListener(@NotNull Bridge bridge, @NotNull CommandRegistry commandRegistry, @NotNull CommandExecutor executor){
        this.bridge = bridge;
        this.commandRegistry = commandRegistry;
        this.executor = executor;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //String message = event.getMessage().getContentDisplay();
        //String channelId = event.getChannel().getId();
        //bridge.sendToTwitch(message);
        //TODO
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Optional<Command> commandOptional = commandRegistry.find(event.getName());
        if(commandOptional.isEmpty()) {
            return;
        }
        event.deferReply(true).queue();
        List<String> optionStrings = event.getOptions().parallelStream().map(OptionMapping::getAsString).toList();
        executor.submit(commandOptional.get(), optionStrings).thenAccept((replyText) -> event.getHook().sendMessage(replyText).queue());
    }
}
