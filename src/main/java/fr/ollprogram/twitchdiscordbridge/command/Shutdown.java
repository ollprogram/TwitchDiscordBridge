

package fr.ollprogram.twitchdiscordbridge.command;

import fr.ollprogram.twitchdiscordbridge.Bridge;
import org.jetbrains.annotations.NotNull;

public class Shutdown implements Command{
    private final Bridge bridge;


    public Shutdown(@NotNull Bridge bridge){
        this.bridge = bridge;
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public void execute() {
        bridge.shutdown();
    }
}
