package ie.iamshanedoyle.craftbeers.utils;

import android.os.Build;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN)
public class CompatUtilsTest {

    @Test
    @Config(emulateSdk = Build.VERSION_CODES.LOLLIPOP)
    public void testIsLollipopOrAbove() throws Exception {
        Assert.assertTrue("IsLollipopOrAbove should be true", CompatUtils.isLollipopOrAbove());
    }

    @Test
    public void testIsJellyBeanOrAbove() throws Exception {
        Assert.assertTrue("IsJellyBeanOrAbove should be true", CompatUtils.isJellyBeanOrAbove());
    }

    @Test
    public void testIsLollipopOrAbove_isFalse() throws Exception {
        Assert.assertFalse("IsLollipopOrAbove should be false", CompatUtils.isLollipopOrAbove());
    }
}