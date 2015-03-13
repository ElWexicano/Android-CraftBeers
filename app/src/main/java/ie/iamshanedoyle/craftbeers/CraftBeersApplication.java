package ie.iamshanedoyle.craftbeers;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by shane on 13/03/15.
 */
public class CraftBeersApplication extends Application {

    private static final String APP_ID = "UA-60730724-1";

    GoogleAnalytics analyticsInstance;
    Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        analyticsInstance = GoogleAnalytics.getInstance(this);
        tracker = analyticsInstance.newTracker(APP_ID);
        tracker.enableAdvertisingIdCollection(true);
    }

    public Tracker getTracker() {
        return tracker;
    }
}
