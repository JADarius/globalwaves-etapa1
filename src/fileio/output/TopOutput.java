package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class TopOutput {
    private String command;
    private int timestamp;
    private ArrayList<String> result;

    public TopOutput(CommandInput query) {
        this.command = query.getCommand();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }
}
