package ie.iamshanedoyle.craftbeers.receivers;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
public class NetworkStateReceiverTest {

    @Test
    public void testIsListening() {
        ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
        Intent intent = new Intent(ConnectivityManager.CONNECTIVITY_ACTION);
        Assert.assertTrue(shadowApplication.hasReceiverForIntent(intent));
    }

}