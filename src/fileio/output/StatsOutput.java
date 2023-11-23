package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;
import utils.Stats;

@Getter
@Setter
public final class StatsOutput extends GenericOutput {
    private Stats stats;

    public StatsOutput(final CommandInput query, final Stats stats) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.stats = stats;
    }
}
