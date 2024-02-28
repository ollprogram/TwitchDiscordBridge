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

package fr.ollprogram.twitchdiscordbridge.listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.ollprogram.twitchdiscordbridge.Bridge;
import org.jetbrains.annotations.NotNull;

public class TwitchListener {

    private final Bridge bridge;

    public TwitchListener(@NotNull Bridge bridge){
        this.bridge = bridge;
    }

    @EventSubscriber
    public void onMessageEvent(ChannelMessageEvent event) {
        String message = event.getMessage();
        String channelId = event.getChannel().getId();
        bridge.sendToDiscord(message, channelId);
    }
}
