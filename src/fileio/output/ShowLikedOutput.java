package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class ShowLikedOutput extends GenericOutput {
    private List<String> result;

    public ShowLikedOutput(final CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }
}
