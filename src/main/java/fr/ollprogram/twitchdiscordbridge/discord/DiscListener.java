package fr.ollprogram.twitchdiscordbridge.discord;

import fr.ollprogram.twitchdiscordbridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.discord.commands.TextCommand;
import fr.ollprogram.twitchdiscordbridge.discord.utils.PrefixUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * The main Discord listener which will read every message events.
 *
 * @author ollprogram
 */
public class DiscListener extends ListenerAdapter {
  private Bridge bridge;
  private TextCommand[] commands;

  /**
   * Set the commands that will be used in the chat.
   *
   * @param commands An array containing all commands.
   */
  public void setCommands(TextCommand[] commands) {
    this.commands = commands;
  }

  /**
   * Set the bridge. By default, there is no bridge (bridge = null).
   *
   * @param bridge The bridge.
   */
  public void setBridge(Bridge bridge) {
    this.bridge = bridge;
  }

  /**
   * Handle every message event. Allows the recognition of a use of a commands in the chat and
   * execute them if possible.
   *
   * @param event The message received event.
   */
  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    if (event.getAuthor().isBot()
        || event.getAuthor().isSystem()
        || !event.isFromGuild()
        || event.isWebhookMessage()
        || commands == null) return;
    String prefix = PrefixUtils.retrievePrefix(event.getGuild());
    TextCommand cmd =
        TextCommand.retrieve(commands, event.getMessage().getContentDisplay(), prefix);
    if (cmd == null) { // if it's not a referenced command
      String content = event.getMessage().getContentDisplay();
      if (content.startsWith(prefix) && content.equalsIgnoreCase(prefix + "help")) {
        helpAction(commands, event);
      } else if (bridge != null
          && bridge.isOpened()
          && event.getChannel().equals(bridge.getDiscordChannel())) {
        bridge.sendToTwitch(event.getMessage());
      }
      return;
    }
    if (cmd.havePermission(event.getMember())) {
      cmd.execute(event);
    } else {
      event
          .getAuthor()
          .openPrivateChannel()
          .queue(
              channel ->
                  channel
                      .sendMessage(
                          event.getAuthor().getAsMention()
                              + " You don't have the permission ["
                              + cmd.getPermName()
                              + "] to use this command.")
                      .queue());
    }
  }

  /**
   * Sends the help privately.
   *
   * @param commands All commands.
   * @param event The message received event.
   */
  private static void helpAction(TextCommand @NotNull [] commands, MessageReceivedEvent event) {
    StringBuilder msg = new StringBuilder();
    msg.append("Commands :\n ```\n");
    for (TextCommand cmd : commands) {
      msg.append("- ")
          .append(cmd.getName())
          .append(" : ")
          .append(cmd.getDescription())
          .append("\n\n");
    }
    msg.append(" ```");
    event
        .getAuthor()
        .openPrivateChannel()
        .queue(channel -> channel.sendMessage(msg.toString()).queue());
  }
}
