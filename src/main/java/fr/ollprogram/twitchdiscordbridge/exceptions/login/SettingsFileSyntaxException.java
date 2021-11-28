package fr.ollprogram.twitchdiscordbridge.exceptions.login;

/**
 * Exception called when the syntax isn't correct in the settings file.
 */
public class SettingsFileSyntaxException extends Exception{

	/**
	 * Construct a new settings file exception.
	 * @param message Error message to print in the console.
	 */
	public SettingsFileSyntaxException(String message){
		super(message);
	}
}
