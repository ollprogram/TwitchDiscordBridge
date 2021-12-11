package fr.ollprogram.twitchdiscordbridge.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Commands for the console.
 * @author ollprogram
 */
public abstract class ConsoleCommand {
	private final String name;

	/**
	 * Construct a console command.
	 * @param name The name of the command (how to call it in the console).
	 */
	public ConsoleCommand(String name){
		this.name = name;
	}

	/**
	 * Action of the command.
	 * @param args command arguments.
	 */
	protected abstract void action(String[] args);

	/**
	 * Get the help/manual for the command.
	 * @return The help to use the command.
	 */
	public abstract  String getHelp();

	/**
	 * Get the description of the command.
	 * @return The description of the command.
	 */
	public abstract String getDescription();

	/**
	 * Execute the action of the command with specified parameters.
	 * @param consoleLine The input line in the console.
	 */
	public void execute(@NotNull String consoleLine){
		String[] args = consoleLine.split(" ");
		if(args[0].length() != name.length())return; //avoid for example that !testHello stills working as a test command
		action(args);
	}

	/**
	 * Retrieve a command in an array of console commands with its name.
	 * @param commands The array of console commands.
	 * @param cmdLine The command line (input in the console).
	 * @return The retrieved command in the array with the specified command line. null if no command found.
	 */
	public static @Nullable ConsoleCommand retrieve(ConsoleCommand @NotNull [] commands, String cmdLine){
		for(ConsoleCommand cmd : commands){
			if(cmdLine.toUpperCase().startsWith(cmd.getName().toUpperCase()))return cmd;
		}
		return null;
	}

	/**
	 * Get the name of the command.
	 * @return The name of the command.
	 */
	public String getName() {
		return name;
	}
}
