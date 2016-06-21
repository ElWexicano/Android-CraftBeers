package ie.iamshanedoyle.craftbeers;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Extends the Application.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class CraftBeersApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startCrashlytics();
    }

    private void startCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }
}
