package fr.ollprogram.twitchdiscordbridge.configuration.validate;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.ollprogram.twitchdiscordbridge.Bridge;
import fr.ollprogram.twitchdiscordbridge.configuration.BridgeConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.util.logging.Logger.getGlobal;

public class BFactory implements BridgeFactory {

    private Optional<JDA> discordBot;

    private Optional<TwitchClient> twitchBot;


    public BFactory(){
        this.discordBot = Optional.empty();
        this.twitchBot = Optional.empty();
    }

    @Override
    public void validate(@NotNull BridgeConfig conf) {
        try { //TODO this try catch this is bad smell (please change in next version with an oauth2 validator)
            discordBot = Optional.of(JDABuilder.createDefault(conf.getDiscordToken()).build());
            OAuth2Credential twitchCred = new OAuth2Credential("twitch", conf.getTwitchToken());
            TwitchClientBuilder builder = TwitchClientBuilder.builder().withChatAccount(twitchCred);
            TwitchClient twitchClient = builder.build();
            twitchBot = Optional.ofNullable(twitchClient);
            if(twitchClient == null) throw new InvalidTokenException();
        } catch (InvalidTokenException e){
            getGlobal().warning("Invalid configuration, please check your tokens.");
            discordBot = Optional.empty();
        }
    }

    @Override
    public boolean isValid(){
        if(discordBot.isEmpty() || twitchBot.isEmpty()) return false;
        else return true;
    }

    @Override
    public @NotNull Bridge createBridge() {
        return null; //TODO
    }
}
