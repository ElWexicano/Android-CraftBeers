package ie.iamshanedoyle.craftbeers.events;

/**
 * This event is used to handle network state changes.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class NetworkStateChangedEvent {

    private boolean mIsInternetConnected;

    public NetworkStateChangedEvent(boolean isInternetConnected) {
        this.mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }
}
