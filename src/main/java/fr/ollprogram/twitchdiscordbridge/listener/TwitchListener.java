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
