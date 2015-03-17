package ie.iamshanedoyle.craftbeers;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

/**
 * Extends the Application.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class CraftBeersApplication extends Application {

    private static final String APP_ID = "UA-60730724-1";

    GoogleAnalytics analyticsInstance;
    Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        startCrashlytics();
        startGoogleAnalytics();
    }

    public Tracker getTracker() {
        return tracker;
    }

    private void startCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }

    private void startGoogleAnalytics() {
        analyticsInstance = GoogleAnalytics.getInstance(this);
        tracker = analyticsInstance.newTracker(APP_ID);
        tracker.enableAdvertisingIdCollection(true);
    }
}
