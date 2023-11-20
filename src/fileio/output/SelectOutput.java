package fileio.output;

import fileio.input.CommandInput;

import java.util.ArrayList;

public class SelectOutput extends GenericOutput {
    private String message;

    public SelectOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
