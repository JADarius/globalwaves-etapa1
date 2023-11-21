package fileio.output;

import fileio.input.CommandInput;

public class PlayPauseOutput extends GenericOutput {
    private String message;

    public PlayPauseOutput(CommandInput query) {
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
