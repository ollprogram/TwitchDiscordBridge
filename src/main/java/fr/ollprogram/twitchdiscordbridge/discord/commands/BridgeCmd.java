package fr.ollprogram.twitchdiscordbridge.discord.commands;

import fr.ollprogram.twitchdiscordbridge.Bridge;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A bridge command for discord. Config or show information about the bridge.
 */
public class BridgeCmd extends TextCommand{
	private final Bridge bridge;

	/**
	 * Construct a new command with the specified name and permission.
	 * @param name The name of the command (how you want to call it on discord).
	 * @param perm The permission needed to be able to use this command.
	 * @param bridge The bridge.
	 */
	public BridgeCmd(String name, Permission perm, Bridge bridge){
		super(name, perm);
		this.bridge = bridge;
	}

	/**
	 * Can change the discord channel targeted. Can open or close the bridge or get information about it.
	 * @param args Parameters for the action that will be executed. First arg is always "".
	 * @param event The message event received.
	 */
	@Override
	protected void action(@NotNull String[] args, MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		if(args.length >= 2){
			if(args[1].equalsIgnoreCase("target")){
				bridge.changeDiscordChannel(channel);
				channel.sendMessage(event.getAuthor().getAsMention()+" Target channel updated: "+channel.getName()).queue();
				return;
			}
			if(args[1].equalsIgnoreCase("open")){
				bridge.open();
				channel.sendMessage(event.getAuthor().getAsMention()+" The bridge is opened").queue();
				return;
			}
			if(args[1].equalsIgnoreCase("close")){
				bridge.close();
				channel.sendMessage(event.getAuthor().getAsMention()+" The bridge is closed").queue();
				return;
			}
			if(args[1].equalsIgnoreCase("info")){
				channel.sendMessage(event.getAuthor().getAsMention()+" Here are the information about the bridge: \n"+
						"```scala\nDiscordTargetChannelName : "+bridge.getDiscordChannel().getName()+"\n"+
						"TwitchTargetChannelName : "+bridge.getTwitchChannelName()+"\n"+
						"Status : "+((bridge.isOpened())?"opened":"closed")+"```").queue();
				return;
			}
		}
		channel.sendMessage(event.getAuthor().getAsMention()+" Wrong argument").queue();
	}

	/**
	 * Get the description of the command.
	 *
	 * @return Description of the command.
	 */
	@Override
	public String getDescription() {
		return "Configs or shows information about the bridge. Parameters : \n" +
				"	- info : Gives information about the bridge.\n" +
				"	- target : Changes the discord targeted channel to the channel where the command have been called.\n" +
				"	- open : Opens the bridge (allows the bot to listen).\n" +
				"	- close : Closes the bridge (the bot can't listen).";
	}

}
