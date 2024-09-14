

package fr.ollprogram.twitchdiscordbridge.command;

import fr.ollprogram.twitchdiscordbridge.Bridge;
import org.jetbrains.annotations.NotNull;

public class Shutdown implements Command{
    private final Bridge bridge;

    private static final String MANUAL = """
            The Shutdown command :
                Shutdown the TwitchDiscordBridge application.
                Examples : 
                    shutdown
                    shutdown now
            """;

    public Shutdown(@NotNull Bridge bridge){
        this.bridge = bridge;
    }

    @Override
    public @NotNull String getName() {
        return "shutdown";
    }

    @Override
    public @NotNull String getHelp() {
        return MANUAL;
    }

    @Override
    public void execute() {
        bridge.shutdown();
    }
}
