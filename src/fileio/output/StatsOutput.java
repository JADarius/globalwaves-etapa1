package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;
import utils.Stats;

@Getter
public final class StatsOutput extends GenericOutput {
    private Stats stats;

    public StatsOutput(CommandInput query, Stats stats) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.stats = stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
