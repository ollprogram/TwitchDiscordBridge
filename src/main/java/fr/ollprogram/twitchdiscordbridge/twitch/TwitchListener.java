package fr.ollprogram.twitchdiscordbridge.twitch;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.ollprogram.twitchdiscordbridge.Bridge;

/**
 * The main event listener for Twitch.
 *
 * @author ollprogram
 */
public class TwitchListener {
  private final Bridge bridge;

  /**
   * Construct the listener with the specified bridge.
   *
   * @param bridge The bridge.
   */
  public TwitchListener(Bridge bridge) {
    this.bridge = bridge;
  }

  @EventSubscriber
  public void onChannelMessage(ChannelMessageEvent event) {
    if (bridge.isOpened()) {
      bridge.sendToDiscord(event);
    }
  }
}
