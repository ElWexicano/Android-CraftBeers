package ie.iamshanedoyle.craftbeers.services;

import android.content.Intent;
import android.os.Build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
public class BeersServiceTest {

    @Test
    public void testStartActionGetBeers() throws Exception {
        BeersService.startActionGetBeer(RuntimeEnvironment.application, "1234tastyBeer");

        Intent serviceIntent = Shadows.shadowOf(
                RuntimeEnvironment.application).peekNextStartedService();
        Assert.assertNotNull(serviceIntent);
        Assert.assertEquals(BeersService.class.getCanonicalName(),
                serviceIntent.getComponent().getClassName());

        Assert.assertTrue(serviceIntent.hasExtra(BeersService.EXTRA_BEER_ID));
    }

    @Test
    public void testStartActionGetBeer() throws Exception {
        BeersService.startActionGetBeers(RuntimeEnvironment.application, "guinness", 1);

        Intent serviceIntent = Shadows.shadowOf(
                RuntimeEnvironment.application).peekNextStartedService();
        Assert.assertNotNull(serviceIntent);
        Assert.assertEquals(BeersService.class.getCanonicalName(),
                serviceIntent.getComponent().getClassName());

        Assert.assertTrue(serviceIntent.hasExtra(BeersService.EXTRA_BEER_KEYWORD));
        Assert.assertTrue(serviceIntent.hasExtra(BeersService.EXTRA_PAGE));
    }

}