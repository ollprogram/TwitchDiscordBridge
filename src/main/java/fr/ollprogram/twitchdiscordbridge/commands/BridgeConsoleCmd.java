package fr.ollprogram.twitchdiscordbridge.commands;

import fr.ollprogram.twitchdiscordbridge.Bridge;
import org.jetbrains.annotations.NotNull;

/**
 * A bridge command for the console.
 *
 * @author ollprogram
 */
public class BridgeConsoleCmd extends ConsoleCommand {

  private final Bridge bridge;

  /**
   * Construct a bridge console command.
   *
   * @param name The name of the command (how to call it in the console).
   * @param bridge The bridge between twitch and discord.
   */
  public BridgeConsoleCmd(String name, Bridge bridge) {
    super(name);
    this.bridge = bridge;
  }

  /**
   * Get the description of the command.
   *
   * @return The description of the command.
   */
  @Override
  public String getDescription() {
    return "This command is used to manage the bridge between Discord and twitch.";
  }

  /**
   * Get the help/manual for the command.
   *
   * @return The help to use the command.
   */
  @Override
  public String getHelp() {
    return getDescription()
        + "\n"
        + "Parameters: \n"
        + "open    Open the bridge\n"
        + "close   Close the bridge\n"
        + "info    Get information about the bridge object\n"
        + "help    Display the description of this command\n"
        + "twitchTarget <channelName> Change the twitch channel targeted";
  }

  /**
   * Open, close, give information about the bridge. It can also change the twitch channel targeted.
   *
   * @param args Parameters : open, close, info, help, twitchTarget target.
   */
  @Override
  protected void action(String @NotNull [] args) {
    if (args.length >= 2) {
      if (args[1].equalsIgnoreCase("open")) {
        bridge.open();
        System.out.println("The bridge is opened.");
        return;
      }
      if (args[1].equalsIgnoreCase("close")) {
        bridge.close();
        System.out.println("The bridge is closed.");
        return;
      }
      if (args[1].equalsIgnoreCase("info")) {
        System.out.println("Info: " + bridge.toString());
        return;
      }
      if (args[1].equalsIgnoreCase("help")) {
        System.out.println(getHelp());
        return;
      }
      if (args.length >= 3 && args[1].equalsIgnoreCase("twitchTarget")) {
        bridge.changeTwitchChannel(args[2]);
        System.out.println("Twitch target changed.");
        return;
      }
    }
    System.out.println("Wrong argument.");
  }
}
