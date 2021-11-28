package fr.ollprogram.twitchdiscordbridge.commands;

import com.github.twitch4j.TwitchClient;
import net.dv8tion.jda.api.JDA;

/**
 * A shutdown command for the console.
 */
public class Shutdown extends ConsoleCommand{

	private final JDA jda;
	private final TwitchClient twitchClient;

	/**
	 * Construct a console command.
	 * @param name The name of the command (how to call it in the console).
	 */
	public Shutdown(String name, JDA jda, TwitchClient twitchClient){
		super(name);
		this.jda = jda;
		this.twitchClient = twitchClient;
	}

	/**
	 * Get the description of the command.
	 * @return The description of the command.
	 */
	@Override
	public String getDescription() {
		return "This command is used to shutdown the application.";
	}

	/**
	 * Get the help/manual for the command.
	 * @return The help to use the command.
	 */
	@Override
	public String getHelp() {
		return getDescription()+"\n"+
				"Parameters: \n" +
				"now    Shutdown with possible errors\n"+
				"help   Display the description of the command\n" +
				"Usage example: \n" +
				">shutdown\n" +
				">shutdown now";
	}

	/**
	 * Shutdown or "shutdown now" both bots.
	 * @param args Parameters : Now, help or nothing.
	 */
	@Override
	protected void action(String[] args) {
		if(args.length >= 2){
			if(args[1].equalsIgnoreCase("Now")){
				twitchClient.close();
				jda.shutdownNow();
				System.out.println("Please wait (could take a few seconds)...");
				return;
			}
			if(args[1].equalsIgnoreCase("help")){
				System.out.println(getHelp());
			}
		}
		else{
			twitchClient.close();
			jda.shutdown();
			System.out.println("Please wait (could take a few seconds)...");
		}

	}
}
