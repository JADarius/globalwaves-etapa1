package utils;

import lombok.Getter;

@Getter
public final class Stats {
    private String name;
    private int remainedTime;
    private String repeat;
    private boolean shuffle;
    private boolean paused;

    public Stats(final String name, final int remainedTime,
                 final int repeat, final boolean shuffle, final boolean paused, final Enums.PlayerType type) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.shuffle = shuffle;
        this.paused = paused;
        if (type == Enums.PlayerType.PLAYLIST) {
            switch (repeat) {
                case 0:
                    this.repeat = "No Repeat";
                    break;
                case 1:
                    this.repeat = "Repeat All";
                    break;
                case 2:
                    this.repeat = "Repeat Current Song";
                    break;
                default:
                    break;
            }
        } else {
            switch (repeat) {
                case 0:
                    this.repeat = "No Repeat";
                    break;
                case 1:
                    this.repeat = "Repeat Once";
                    break;
                case 2:
                    this.repeat = "Repeat Infinite";
                    break;
                default:
                    break;
            }
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRemainedTime(final int remainedTime) {
        this.remainedTime = remainedTime;
    }

    public void setRepeat(final String repeat) {
        this.repeat = repeat;
    }

    public void setShuffle(final boolean shuffle) {
        this.shuffle = shuffle;
    }

    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
}
