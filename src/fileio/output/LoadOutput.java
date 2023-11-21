package fileio.output;

import fileio.input.CommandInput;

public class LoadOutput extends GenericOutput {
    private String message;

    public LoadOutput(CommandInput query) {
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
