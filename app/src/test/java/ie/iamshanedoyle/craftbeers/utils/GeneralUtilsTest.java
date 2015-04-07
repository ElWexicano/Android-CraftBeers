package ie.iamshanedoyle.craftbeers.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class GeneralUtilsTest {

    @Test
    public void testInterpolate() throws Exception {
        fail("Not tested yet!");
    }

    @Test
    public void testGetApplicationVersion() throws Exception {
        assertNotNull("Application Version is null",
                GeneralUtils.getApplicationVersion(Robolectric.application));
    }
}