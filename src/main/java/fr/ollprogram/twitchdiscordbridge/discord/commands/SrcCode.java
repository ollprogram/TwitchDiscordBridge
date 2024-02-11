package fr.ollprogram.twitchdiscordbridge.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Command to get the source code of this app.
 *
 * @author ollprogram
 */
public class SrcCode extends TextCommand {

  /**
   * Construct a new command with the specified name and permission.
   *
   * @param name The name of the command (how you want to call it on discord).
   */
  public SrcCode(String name) {
    super(name);
  }

  /**
   * Send the source code.
   *
   * @param args Parameters for the action that will be executed. First arg is always "".
   * @param event The message event received.
   */
  @Override
  protected void action(@NotNull String[] args, @NotNull MessageReceivedEvent event) {
    event
        .getChannel()
        .sendMessage(
            "Bonjour "
                + event.getAuthor().getAsMention()
                + "! I'm ollprogram, also known as Olleroy."
                + "\n"
                + " I built a program to link the Twitch chat and the Discord chat together.\n"
                + "Here is the source code : https://github.com/ollprogram/TwitchDiscordBridge")
        .queue();
  }

  /**
   * Get the description of the command.
   *
   * @return Description of the command.
   */
  @Override
  public String getDescription() {
    return "Gives the source code of this application.";
  }
}
