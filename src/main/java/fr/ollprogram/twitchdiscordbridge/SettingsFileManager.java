package fr.ollprogram.twitchdiscordbridge;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import fr.ollprogram.twitchdiscordbridge.exceptions.login.SettingsFileSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represent all settings present in the settings file.
 * These settings are important for the application to save information for the next relaunch.
 * Settings are always updated in files when modified.
 * @author ollprogram
 */
public class SettingsFileManager {
	private OAuth2Credential twitchAccount;
	private String discordToken;
	private String discordChannelID;
	private String twitchChannelName;

	/**
	 * Instantiate a settingsFile object and try to retrieve and update tokens.
	 * @throws IOException if it can't rewrite settings file.
	 */
	public SettingsFileManager() throws IOException{
		try {
			System.out.println("[INFO] Trying to read settings...");
			loadFromFile();
		}catch(IOException | SettingsFileSyntaxException e){//can't load the file
			System.out.println("[INFO] You don't have yet defined your settings.");

			//demands
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your twitch token :");
			this.twitchAccount = new OAuth2Credential( "twitch", sc.nextLine());
			System.out.println("Enter your discord token :");
			this.discordToken =  sc.nextLine();
			System.out.println("Enter your discord channel ID for the app :");
			this.discordChannelID = sc.nextLine();
			System.out.println("Enter your twitch channel name for the app :");
			this.twitchChannelName = sc.nextLine();
			sc.close();
			update();
		}
		System.out.println("[INFO] Settings successfully read.");
	}

	/**
	 * Read all lines of the settings file and catch information.
	 * @param lines Lines from the settings file.
	 */
	private void fillFields(@NotNull List<String> lines) {
		for(String l : lines){
			if(l.startsWith("Twitch_Token=")){
				twitchAccount = new OAuth2Credential( "twitch", l.replace("Twitch_Token=", ""));
			}
			else if(l.startsWith("Discord_Token=")){
				discordToken = l.replace("Discord_Token=",  "");
			}
			else if(l.startsWith("Discord_Channel_ID=")){
				discordChannelID = l.replace("Discord_Channel_ID=",  "");
			}
			else if(l.startsWith("Twitch_Channel_Name=")){
				twitchChannelName = l.replace("Twitch_Channel_Name=", "");
			}
		}
	}

	/**
	 * A string is undefined when it is null or "".
	 * @param s the String field.
	 * @return if the string field is not defined.
	 */
	private static boolean isUndefined(String s){
		return (s == null || s.equals(""));
	}

	/**
	 * Load information like tokens or IDs into settings.txt .
	 * @throws IOException if the file is not available.
	 * @throws SettingsFileSyntaxException if the settings file is not correct.
	 */
	private void loadFromFile() throws IOException, SettingsFileSyntaxException {
		File file = new File("settings.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<String> lines = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		fillFields(lines);
		if (isUndefined(discordToken) || isUndefined(discordChannelID)
				|| isUndefined(twitchChannelName) || twitchAccount == null) {
			throw new SettingsFileSyntaxException("WRONG settings contents : \n" + file.getAbsolutePath());
		}
	}

	/**
	 * Write information retrieved and ids in the settings file.
	 * @throws IOException If it can't find, write or rewrite a file.
	 */
	private void update() throws IOException {
		File file = new File("settings.txt");
		if(file.createNewFile()){
			FileWriter writer = new FileWriter(file);
			writer.write("Twitch_Token="+this.twitchAccount.getAccessToken()+"\n");
			writer.write("Discord_Token="+this.discordToken+"\n");
			writer.write("Discord_Channel_ID="+this.discordChannelID+"\n");
			writer.write("Twitch_Channel_Name="+this.twitchChannelName);
			System.out.println("[INFO] Settings updated at : "+file.getAbsolutePath());
			writer.close();
		}
		else{
			if(file.delete()){
				update();
			}
			else System.out.println("[WARN] Can't rewrite the file.");
		}
	}

	//UPDATERS

	/**
	 * Update the discord channel ID.
	 * @param channelID The new channel ID.
	 */
	public void updateDiscordChannelID(String channelID){
		this.discordChannelID = channelID;
		try {
			update();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("[WARN] Something went wrong when we tried to update the settings file");
		}
	}

	/**
	 * Update the twitch channel name.
	 * @param channelName The new channel name.
	 */
	public void updateTwitchChannelName(String channelName){
		this.twitchChannelName = channelName;
		try {
			update();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("[WARN] Something went wrong when we tried to update the settings file");
		}
	}

	//GETTERS

	/**
	 * Get the discord token.
	 * @return The discord token.
	 */
	public String getDiscordToken() {
		return discordToken;
	}

	/**
	 * Get the twitch account of the bot.
	 * @return The twitch account of the bot.
	 */
	public OAuth2Credential getTwitchAccount() {
		return twitchAccount;
	}

	/**
	 * Get the discord channel ID.
	 * @return The discord channel ID.
	 */
	public String getDiscordChannelID() {
		return discordChannelID;
	}

	/**
	 * Get the twitch channel name.
	 * @return The twitch channel name.
	 */
	public String getTwitchChannelName() {
		return twitchChannelName;
	}

}
