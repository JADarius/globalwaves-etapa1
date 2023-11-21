package fileio.output;

import fileio.input.CommandInput;
import utils.Stats;

public class StatsOutput extends GenericOutput {
    private Stats stats;

    public StatsOutput(CommandInput query, Stats stats) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.stats = stats;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
