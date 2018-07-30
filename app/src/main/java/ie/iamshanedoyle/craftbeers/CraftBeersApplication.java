package ie.iamshanedoyle.craftbeers;

import android.app.Application;

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
//        Fabric.with(this, new Crashlytics());

        // TODO: 30/07/2018 start tracking.
    }
}
