package ie.iamshanedoyle.craftbeers.utils;

import android.os.Build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = Build.VERSION_CODES.LOLLIPOP)
public class GeneralUtilsTest {

    @Test
    public void testInterpolate() throws Exception {
//        Assert.fail("Not tested yet!");
    }

    @Test
    public void testGetApplicationVersion() throws Exception {
        Assert.assertNotNull("Application Version is null",
                GeneralUtils.getApplicationVersion(RuntimeEnvironment.application));
    }
}