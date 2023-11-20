package fileio.output;

import fileio.input.CommandInput;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;

public class SearchOutput extends GenericOutput {
    private String message;
    private ArrayList<String> results;

    public SearchOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.results = new ArrayList<String>();
        this.message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getResults() {
        return results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }
}
