package ie.iamshanedoyle.craftbeers.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import de.greenrobot.event.EventBus;
import ie.iamshanedoyle.craftbeers.events.NetworkStateChangedEvent;

/**
 * This is the base activity.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trackScreen();
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

    /**
     * Google Analytics Screen Tracking.
     */
    private void trackScreen() {
        FirebaseAnalytics firebaseAnalytics =
                FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getScreenName());
        firebaseAnalytics.logEvent("view_screen", bundle);
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
