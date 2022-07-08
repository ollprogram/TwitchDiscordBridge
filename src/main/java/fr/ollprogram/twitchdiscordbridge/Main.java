package fr.ollprogram.twitchdiscordbridge;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.ollprogram.twitchdiscordbridge.commands.BridgeConsoleCmd;
import fr.ollprogram.twitchdiscordbridge.commands.ConsoleCommand;
import fr.ollprogram.twitchdiscordbridge.commands.Say;
import fr.ollprogram.twitchdiscordbridge.commands.Shutdown;
import fr.ollprogram.twitchdiscordbridge.discord.DiscListener;
import fr.ollprogram.twitchdiscordbridge.discord.commands.*;
import fr.ollprogram.twitchdiscordbridge.twitch.TwitchListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author ollprogram
 */
public class Main {

	public static void main(String[] args) throws IOException {
		SettingsFile settings = new SettingsFile();
		System.out.println("[INFO] Your bots must be private, and the bots are recommended to be used on only one server\n");
		System.out.println("[INFO] It's also recommended to disable links for everyone on discord channel target and twitch channel targeted.\n");
		try {
			//START THE TWITCH BOT
			System.out.println("[INFO] Starting the Twitch bot...");
			TwitchClient twitchClient = TwitchClientBuilder.builder()
					.withEnableChat(true)
					.withChatAccount(settings.getTwitchAccount()).build();
			System.out.println("[INFO] Twitch bot started.");

			//START THE DISCORD BOT
			System.out.println("[INFO] Starting the Discord bot...");
			JDABuilder botBuilder = JDABuilder.createDefault(settings.getDiscordToken());
			DiscListener discListener = new DiscListener();
			JDA jda = botBuilder.addEventListeners(discListener).build();
			jda.awaitReady();//wait until the bot reach the connected status
			jda.getPresence().setActivity(Activity.listening("Twitch : "+settings.getTwitchChannelName()));
			Bridge bridge = new Bridge(settings, twitchClient.getChat(), jda);

			TextCommand[] commands = {new Test("test", Permission.ADMINISTRATOR),//define all discord commands
					new Prefix("prefix", Permission.ADMINISTRATOR),
					new SrcCode("code"), new BridgeCmd("bridge", Permission.ADMINISTRATOR, bridge)};
			//setting the bridge and commands for the discord listener
			discListener.setBridge(bridge);
			discListener.setCommands(commands);
			System.out.println("[INFO] Discord bot started.");

			//adding the chat event listener for the twitch bot
			twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchListener(bridge));
			twitchClient.getChat().joinChannel(bridge.getTwitchChannelName());


			//BEGINNING THE CONSOLE INTERFACE IN THIS THREAD
			//define all console commands
			ConsoleCommand[] consoleCommands = {new Shutdown("shutdown", jda, twitchClient), new Say("say", bridge)
			, new BridgeConsoleCmd("bridge", bridge)};
			//launch the interface
			consoleInterface(consoleCommands, jda);

			//ENDING
			System.out.println("[INFO] Shutdown...");
		} catch (LoginException | InterruptedException loginError) {
			loginError.printStackTrace();
			System.out.println("[WARN] Login Error.");
		}
	}

	/**
	 * Handle the console Interface, continue until the jda instance still connected.
	 * @param commands all commands for the console interface.
	 * @param jda the jda instance (discord bot).
	 */
	private static void consoleInterface(ConsoleCommand[] commands, @NotNull JDA jda){
		Scanner sc = new Scanner(System.in);
		System.out.println("[INFO] You can type something in the console : (help to view all commands)");
		while(jda.getStatus().equals(JDA.Status.CONNECTED)){
			System.out.print(">");
			String line = sc.nextLine();
			ConsoleCommand cmd = ConsoleCommand.retrieve(commands, line);
			if(line.equalsIgnoreCase("help")){
				System.out.println("[INFO] All commands: (help for more info except for 'say')");
				for (ConsoleCommand command: commands) {
					System.out.println(command.getName()+"   "+command.getDescription());
				}
			}
			else if(cmd != null){
				cmd.execute(line);
			}
			else{
				System.out.println("[INFO] Not a command.");
			}
		}
		sc.close();
	}
}
