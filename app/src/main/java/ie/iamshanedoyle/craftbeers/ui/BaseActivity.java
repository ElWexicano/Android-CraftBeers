package ie.iamshanedoyle.craftbeers.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import de.greenrobot.event.EventBus;
import ie.iamshanedoyle.craftbeers.events.NetworkStateChangedEvent;

/**
 * This is the base activity.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trackEvent(getScreenName(), getScreenBundle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    protected abstract String getScreenName();

    protected abstract Bundle getScreenBundle();

    /**
     * Google Analytics Event Tracking.
     * @param eventName A String with the name of the event.
     * @param bundle A Bundle of event parameters.
     */
    protected void trackEvent(String eventName, Bundle bundle) {
        FirebaseAnalytics firebaseAnalytics =
                FirebaseAnalytics.getInstance(this);

        firebaseAnalytics.logEvent(eventName, bundle);
    }

    /**
     * Called when we receive a NetworkStateChangedEvent. Generally because the internet connection
     * has dropped or come back on.
     *
     * @param networkStateChangedEvent A NetworkStateChangedEvent.
     */
    @SuppressWarnings("unused")
    public void onEventMainThread(NetworkStateChangedEvent networkStateChangedEvent) {
        if (networkStateChangedEvent.isInternetConnected()) {
            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
