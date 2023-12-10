package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class AnonymousOutput {
    private String command;
    private ArrayList<String> result;
    private int timestamp;

    public AnonymousOutput(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.result = new ArrayList<>();
    }
}
