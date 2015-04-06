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

    private GoogleAnalytics mAnalyticsInstance;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        startCrashlytics();
        startGoogleAnalytics();
    }

    public Tracker getTracker() {
        return mTracker;
    }

    private void startCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }

    private void startGoogleAnalytics() {
        mAnalyticsInstance = GoogleAnalytics.getInstance(this);
        mTracker = mAnalyticsInstance.newTracker(APP_ID);
        mTracker.enableAdvertisingIdCollection(true);
    }
}
