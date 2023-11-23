package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MessageOutput extends GenericOutput {
    private String message;

    public MessageOutput(final CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.message = "";
    }
}
