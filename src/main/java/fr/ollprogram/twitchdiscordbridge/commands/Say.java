package fr.ollprogram.twitchdiscordbridge.commands;

import fr.ollprogram.twitchdiscordbridge.Bridge;

/**
 * A say command for the console.
 */
public class Say extends ConsoleCommand{

	private final Bridge bridge;

	/**
	 * Construct a console say command.
	 * @param name The name of the command (how to call it in the console).
	 * @param bridge The bridge.
	 */
	public Say(String name, Bridge bridge){
		super(name);
		this.bridge = bridge;
	}

	/**
	 * Get the description of the command.
	 * @return The description of the command.
	 */
	@Override
	public String getDescription() {
		return "This command is used to send a message on both platforms";
	}

	/**
	 * Get the help/manual for the command.
	 * @return The help to use the command.
	 */
	@Override
	public String getHelp() {
		return null;
	}

	/**
	 * Unsplit a part of the array already split.
	 * @param sliced The already split array.
	 * @return The unsplit string without the first element.
	 */
	private static String unsplitAPart(String[] sliced){
		StringBuilder res = new StringBuilder();
		for(int i = 1; i < sliced.length; i++){
			res.append(" ").append(sliced[i]);
		}
		return res.toString();
		//intelliJ said that I should use a string builder instead of using '+=' ¯\_(ツ)_/¯
	}

	/**
	 * Say something in both chats.
	 * @param args Including the text to send.
	 */
	@Override
	protected void action(String[] args) {
		if(args.length >= 2) {
			String message = unsplitAPart(args);//The split -> unsplit remove all useless spaces and avoid jda exceptions
			bridge.getBotChat().sendMessage(bridge.getTwitchChannelName(), message);
			bridge.getDiscordChannel().sendMessage(message).queue();
			System.out.println("Message sent to both platforms");
		}
	}
}
