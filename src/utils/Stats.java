package utils;

public class Stats {
    private String name;
    private int remainedTime;
    private String repeat;
    private boolean shuffle;
    private boolean paused;

    public Stats(String name, int remainedTime, int repeat, boolean shuffle, boolean paused, int type) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.shuffle = shuffle;
        this.paused = paused;
        if (type == 2) {
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
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public void setRemainedTime(int remainedTime) {
        this.remainedTime = remainedTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
