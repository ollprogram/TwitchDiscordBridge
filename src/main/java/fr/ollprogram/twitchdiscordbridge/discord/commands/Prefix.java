package fr.ollprogram.twitchdiscordbridge.discord.commands;

import fr.ollprogram.twitchdiscordbridge.discord.utils.PrefixUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Allows to change the prefix.
 * @author ollprogram
 */
public final class Prefix extends TextCommand {

	/**
	 * Construct a new command with the specified name and permission.
	 * @param name The name of the command (how you want to call it on discord).
	 * @param perm The permission needed to be able to use this command.
	 */
	public Prefix(String name, Permission perm){
		super(name, perm);
	}

	/**
	 * Can change the discord prefix for commands.
	 * @param args Parameters for the action that will be executed. First arg is always "".
	 * @param event The message event received.
	 */
	@Override
	protected void action(@NotNull String @NotNull [] args, MessageReceivedEvent event) {
		if(args.length >= 2){
			PrefixUtils.setPrefix(event.getGuild(), args[1]); //args 0 is always empty.
			event.getChannel().sendMessage("The prefix has been changed with success : "+args[1]).queue();
		}
	}

	/**
	 * Get the description of the command.
	 *
	 * @return Description of the command.
	 */
	@Override
	public @NotNull String getDescription() {
		return "Changes the prefix to another. Parameters : \n" +
				"	- the new prefix (ex: ?)\n" +
				"	- reset : reset the prefix to !";
	}
}
