package ie.iamshanedoyle.craftbeers.models;

import android.os.Build;
import android.os.Parcel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = Build.VERSION_CODES.LOLLIPOP)
public class BeerTest {

    private Beer mBeer;

    @Before
    public void setUp() throws Exception {
        mBeer = new Beer();
    }

    @Test
    public void testPreconditions() throws Exception {
        Assert.assertNotNull("Beer is null", mBeer);
    }

    @Test
    public void testGetStyleAsString() throws Exception {
        Assert.assertNotNull("StyleAsString is null", mBeer.getStyleAsString());

        String styleName = "Tasty Scrumptious Beer Style";
        Style style = Mockito.mock(Style.class);
        Mockito.when(style.getName()).thenReturn(styleName);
        mBeer.setStyle(style);
        Assert.assertEquals(styleName, mBeer.getStyleAsString());
    }

    @Test
    public void testGetIbuAsString() throws Exception {
        Assert.assertNotNull("IbuAsString is null", mBeer.getIbuAsString());

        String ibu = "10%";
        mBeer.setIbu(ibu);
        Assert.assertEquals(ibu, mBeer.getIbuAsString());
    }

    @Test
    public void testGetAbvAsString() throws Exception {
        Assert.assertNotNull("AbvAsString is null", mBeer.getAbvAsString());

        String abv = "10%";
        mBeer.setAbv(abv);
        Assert.assertEquals(abv, mBeer.getAbvAsString());
    }

    @Test
    public void testGetGlassAsString() throws Exception {
        Assert.assertNotNull("GlassAsString is null", mBeer.getGlassAsString());

        String pintGlass = "Pint";
        Glass glass = Mockito.mock(Glass.class);
        Mockito.when(glass.getName()).thenReturn(pintGlass);
        mBeer.setGlass(glass);
        Assert.assertEquals(pintGlass, mBeer.getGlassAsString());
    }

    @Test
    public void testGetYearAsString() throws Exception {
        Assert.assertNotNull("YearAsString is null", mBeer.getYearAsString());

        int year = 1986;
        mBeer.setYear(year);
        Assert.assertEquals(Integer.toString(year), mBeer.getYearAsString());
    }

    @Test
    public void testParcelable() throws Exception {
        Parcel parcel = Parcel.obtain();
        mBeer.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Beer beer = Beer.CREATOR.createFromParcel(parcel);
        Assert.assertNotNull("Beer is null", beer);
    }

    @Test
    public void testDescribeContents() throws Exception {
        Assert.assertTrue(mBeer.describeContents() == 0);
    }
}