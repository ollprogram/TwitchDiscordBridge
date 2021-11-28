package fr.ollprogram.twitchdiscordbridge.discord.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

/**
 * Tools for prefix manipulations
 */
public abstract class PrefixUtils {
	/**
	 * Retrieve the prefix in the specified guild
	 * @param guild The guild
	 * @return The prefix which belongs to the specified guild
	 */
	public static String retrievePrefix(Guild guild) {
		Member botMember = guild.getSelfMember();
		String botNickname = botMember.getNickname();
		if(botNickname == null) return "!";
		StringBuilder prefixBuilder = new StringBuilder();
		if(botNickname.charAt(0) == '<'){
			for(int i = 1; i < botNickname.length()-1 && botNickname.charAt(i) != '>'; i++){
				prefixBuilder.append(botNickname.charAt(i));
				if(i == botNickname.length()-2 && botNickname.charAt(botNickname.length()-1) != '>') return "!";
			}
			return prefixBuilder.toString();
		}
		return "!";
	}

	/**
	 * Set the prefix
	 * @param guild The guild
	 * @param newPrefix The new prefix to set. <code>"reset"</code> to reset the prefix and the bot nickname.
	 */
	public static void setPrefix(Guild guild, String newPrefix){
		Member botMember = guild.getSelfMember();
		if(newPrefix.equalsIgnoreCase("reset")){
			botMember.modifyNickname("").queue();
			return;
		}
		if(!newPrefix.equalsIgnoreCase("")){
			botMember.modifyNickname("<"+newPrefix+"> "+botMember.getUser().getName()).queue();
		}
	}
}
