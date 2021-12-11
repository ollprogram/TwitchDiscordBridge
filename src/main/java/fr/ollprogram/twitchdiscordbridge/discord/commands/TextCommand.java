package fr.ollprogram.twitchdiscordbridge.discord.commands;

import fr.ollprogram.twitchdiscordbridge.discord.utils.PrefixUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Describe all discord chat textual commands.
 * @author ollprogram
 */
public abstract class TextCommand {
	private final String name;
	private final Permission perm;

	/**
	 * Construct a new command with the specified name and permission.
	 * @param name The name of the command (how you want to call it on discord).
	 * @param perm The permission needed to be able to use this command.
	 */
	public TextCommand(String name, Permission perm){
		this.name = name;
		this.perm = perm;
	}

	/**
	 * Construct a new command with the specified name and permission.
	 * @param name The name of the command (how you want to call it on discord).
	 */
	public TextCommand(String name){
		this.name = name;
		this.perm = null;
	}

	/**
	 * Check if a member have the permission to use the command.
	 * @param member The member.
	 * @return If the member have the permission to use the command.
	 */
	public boolean havePermission(Member member){
		if(member == null)return false;
		if(perm == null)return true;
		return member.hasPermission(perm);
	}

	/**
	 * The action of the command. It's recommended to override this method for subclasses.
	 * @param args Action that will be executed. First arg is always "".
	 */
	protected abstract void action(@NotNull String[] args, MessageReceivedEvent event);

	/**
	 * Execute the command (the action).
	 * @param event The message received event.
	 */
	public void execute(@NotNull MessageReceivedEvent event){
		final String prefixAndName = PrefixUtils.retrievePrefix(event.getGuild())+name;
		String[] cmd = event.getMessage().getContentDisplay().split(" ");
		if(prefixAndName.length() != cmd[0].length())return;
		action(cmd, event);
	}

	/**
	 * Get the name of the command.
	 * @return The name of the command.
	 */
	public String getName() {return name;}

	/**
	 * Get the permission for using this command.
	 * @return The permission's name to use the command.
	 */
	public String getPermName(){
		if(perm == null)return "NONE";
		return perm.getName();
	}

	/**
	 * Search a command in an array of all commands.
	 * @param commands The array of all created commands.
	 * @param msg The message content of the command.
	 * @param prefix The current prefix.
	 * @return The command or null if not in the array.
	 */
	@Nullable public static TextCommand retrieve(TextCommand @NotNull [] commands, String msg, String prefix){
		for(TextCommand command : commands){
			String comparison = prefix+command.getName();
			if(msg.toUpperCase().startsWith(comparison.toUpperCase()))return command;
		}
		return null;
	}

	/**
	 * Get the description of the command.
	 * @return Description of the command.
	 */
	public abstract String getDescription();
}
