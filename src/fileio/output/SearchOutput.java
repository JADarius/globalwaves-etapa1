package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class SearchOutput extends GenericOutput {
    private String message;
    private ArrayList<String> results;

    public SearchOutput(final CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.results = new ArrayList<String>();
        this.message = "";
    }
}
