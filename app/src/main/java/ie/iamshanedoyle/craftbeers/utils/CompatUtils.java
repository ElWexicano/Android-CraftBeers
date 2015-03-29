package ie.iamshanedoyle.craftbeers.utils;

import android.os.Build;

/**
 * This is a compatibility utility class containing compat methods.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class CompatUtils {

    private CompatUtils() {
        throw new Error("No instances");
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isLollipopOrAbove() {
        return getSdkVersion() >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isKitKatOrAbove() {
        return getSdkVersion() >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isJellyBeanOrAbove() {
        return getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN;
    }
}