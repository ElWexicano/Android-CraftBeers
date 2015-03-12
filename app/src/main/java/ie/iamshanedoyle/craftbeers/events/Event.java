package ie.iamshanedoyle.craftbeers.events;

/**
 * Created by shane on 12/03/15.
 */
public abstract class Event {

    private Status mStatus = Status.FAILED;

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    public boolean isSuccessful() {
        return mStatus == Status.SUCCESSFUL;
    }

    public boolean isFailed() {
        return mStatus == Status.FAILED;
    }

    public enum Status {
        SUCCESSFUL,
        FAILED
    }
}
