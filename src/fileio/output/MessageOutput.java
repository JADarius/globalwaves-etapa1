package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;

@Getter
public final class MessageOutput extends GenericOutput {
    private String message;

    public MessageOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.message = "";
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
