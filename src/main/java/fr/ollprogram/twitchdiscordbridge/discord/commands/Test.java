package fr.ollprogram.twitchdiscordbridge.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Just a command to test or send a little message.
 * @author ollprogram
 */
public class Test extends TextCommand {

	/**
	 * Construct a new command with the specified name and permission.
	 * @param name The name of the command (how you want to call it on discord).
	 * @param perm The permission needed to be able to use this command.
	 */
	public Test(String name, Permission perm) {
		super(name, perm);
	}

	/**
	 * Send a little presentation to the chat.
	 * @param args Parameters for the action that will be executed. First arg is always "".
	 * @param event The message event received.
	 */
	@Override
	protected void action(@NotNull String[] args, @NotNull MessageReceivedEvent event) {
		event.getChannel().sendMessage("Bonjour "+event.getAuthor().getAsMention()+"! I'm ollprogram also known as Olleroy." +
				" I built a program to link the Twitch chat and the Discord chat together.\n"+
				"Here is my Github account : https://github.com/ollprogram?tab=repositories").queue();
	}

	/**
	 * Get the description of the command.
	 *
	 * @return Description of the command.
	 */
	@Override
	public String getDescription() {
		return "Sends a little message.";
	}
}
