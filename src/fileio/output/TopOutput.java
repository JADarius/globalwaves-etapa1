package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class TopOutput {
    private final String command;
    private final int timestamp;
    private ArrayList<String> result;

    public TopOutput(final CommandInput query) {
        this.command = query.getCommand();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }
}
