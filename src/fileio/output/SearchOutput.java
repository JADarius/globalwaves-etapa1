package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;

@Getter
public final class SearchOutput extends GenericOutput {
    private String message;
    private ArrayList<String> results;

    public SearchOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.results = new ArrayList<String>();
        this.message = "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }
}
