package ie.iamshanedoyle.craftbeers.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * This is a general utility class containing general methods.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class GeneralUtils {

    public static int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }

    /**
     * Returns the application version as a string.
     *
     * @param context Context
     * @return String
     */
    public static String getApplicationVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        StringBuilder version = new StringBuilder("");

        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            version.append(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            version.append(0);
        }

        return version.toString();
    }
}
