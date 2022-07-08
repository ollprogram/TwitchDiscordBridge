package fr.ollprogram.twitchdiscordbridge;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.ollprogram.twitchdiscordbridge.discord.utils.FilteringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

/**
 * A bridge represent a link between Twitch and Discord.
 * It allows sending messages on both sides.
 * @author ollprogram
 */
public final class Bridge {
	private final SettingsFile settings;
	private final TwitchChat botChat;
	private MessageChannel discordChannel;
	private String twitchChannelName;
	private boolean isOpened;

	/**
	 * Construct a bridge with specified preferences.
	 * @param settings settings containing channels used.
	 * @param botChat The chat of the Twitch bot.
	 * @param jda The jda instance (Discord bot).
	 */
	public Bridge(@NotNull SettingsFile settings, TwitchChat botChat, @NotNull JDA jda){
		this.settings = settings;
		this.botChat = botChat;
		this.discordChannel = jda.getTextChannelById(settings.getDiscordChannelID());
		this.isOpened = false;
		this.twitchChannelName = settings.getTwitchChannelName();
	}

	/**
	 * Get the Twitch channel name.
	 * @return the Twitch channel name.
	 */
	public String getTwitchChannelName() {
		return twitchChannelName;
	}

	/**
	 * Get the chat of the Twitch bot.
	 * @return The chat of the twitch bot.
	 */
	public TwitchChat getBotChat() {
		return botChat;
	}

	/**
	 * Get the discord channel.
	 * @return The discord channel.
	 */
	public MessageChannel getDiscordChannel() {
		return discordChannel;
	}

	/**
	 * Switch to, and update in settings, the discord channel.
	 * @param channel The new channel.
	 */
	public void changeDiscordChannel(@NotNull MessageChannel channel){
		this.discordChannel = channel;
		settings.updateDiscordChannelID(channel.getId());
	}

	/**
	 * Switch to, and update in settings, the twitch channel name.
	 * @param channelName The name of the twitch channel.
	 */
	public void changeTwitchChannel(String channelName){
		this.twitchChannelName = channelName;
		settings.updateTwitchChannelName(channelName);
	}

	/**
	 * Send a message from discord, to twitch.
	 * @param msg The message to send.
	 */
	public void sendToTwitch(Message msg){
		if(!isOpened)return;
		String messageStripped = msg.getContentStripped();
		String cleanMessage = FilteringUtils.filterNonUniversalEmojis(messageStripped);
		if (cleanMessage.contains("https://tenor.com/view/") || FilteringUtils.isEmptyString(cleanMessage))return;
		//ignoring tenor gifs messages and messages without text content (files)
		botChat.sendMessage(twitchChannelName,
				"[DISCORD] "+msg.getAuthor().getName()+" says : "+ cleanMessage);
	}

	/**
	 * Send a message from twitch, to discord.
	 * @param msg The message to send.
	 */
	public void sendToDiscord(ChannelMessageEvent msg){
		if(!isOpened)return;
		String filteredMessage = FilteringUtils.filterNonUniversalEmojis(msg.getMessage());
		if(FilteringUtils.isEmptyString(filteredMessage))return;
		try{
			discordChannel.sendMessage("[TWITCH] "+msg.getUser().getName()+" says : "+filteredMessage).queue();
		}catch(NullPointerException e) { //if the discord channel isn't defined in settings.
			System.out.println("Something is wrong with the discord channel ID." +
					" Turn off the app. Change the ID in settings and relaunch the app.\n"+
					" You can also use the command <bridge target> into a text channel");
		}
	}

	/**
	 * Check if the bridge is opened.
	 * @return if the bridge is opened.
	 */
	public boolean isOpened() {
		return isOpened;
	}

	/**
	 * Open the bridge.
	 */
	public void open(){
		isOpened = true;
	}

	/**
	 * Close the bridge.
	 */
	public void close(){
		isOpened = false;
	}

	/**
	 * Get a string representation for a bridge object.
	 * @return The representation.
	 */
	@Override
	public @NotNull String toString() {
		return "Bridge{" +
				", discordChannel=" + discordChannel.getName()+"("+discordChannel.getId()+")" +
				", twitchChannelName='" + twitchChannelName + '\'' +
				", isOpened=" + isOpened +
				'}';
	}
}
