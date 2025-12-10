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
import fr.ollprogram.twitchdiscordbridge.command.CommandRegistry;
import fr.ollprogram.twitchdiscordbridge.command.TDBExecutor;
import fr.ollprogram.twitchdiscordbridge.utils.MessageUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Discord main listener. Listen to messages and commands. It can be split into two listeners if the application get bigger
 */
public class DiscordListener extends ListenerAdapter  {

    private final Bridge bridge;

    private final CommandRegistry commandRegistry;

    private final TDBExecutor executor;

    /**
     * Constructor
     * @param bridge The bridge
     * @param commandRegistry The command registry
     * @param executor The TDB executor
     */
    public DiscordListener(@NotNull Bridge bridge, @NotNull CommandRegistry commandRegistry, @NotNull TDBExecutor executor){
        this.bridge = bridge;
        this.commandRegistry = commandRegistry;
        this.executor = executor;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String channelId = event.getChannel().getId();
        User author = event.getAuthor();
        executor.submit(() -> {
            if(!author.isBot() && !author.isSystem() && bridge.isOpen() && bridge.isDiscordTarget(channelId)){
                bridge.sendToTwitch(author.getName()+" says : "+ MessageUtils.filterMessage(message));
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String subcommandName = event.getSubcommandName();
        Optional<Command> commandOptional = (subcommandName == null) ?
                commandRegistry.getCommand(event.getName()) : commandRegistry.getSubcommand(event.getName(), subcommandName);
        if(commandOptional.isEmpty()) {
            return;
        }
        event.deferReply(true).queue();
        List<String> optionStrings = event.getOptions().parallelStream().map(OptionMapping::getAsString).toList();
        executor.submit(commandOptional.get(), optionStrings).thenAccept((replyText) -> event.getHook().sendMessage(replyText).queue());
    }
}
