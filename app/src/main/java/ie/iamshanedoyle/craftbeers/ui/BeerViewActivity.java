package ie.iamshanedoyle.craftbeers.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.models.Beer;
import ie.iamshanedoyle.craftbeers.models.Brewery;

/**
 * This activity is used for displaying information about a beer.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeerViewActivity extends ActionBarActivity {

    private Beer mBeer;
    private ImageView mImageViewLabel;
    private TextView mTextViewTitle;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_view);

        if (getIntent() != null) {
            if (getIntent().hasExtra(BeersActivity.EXTRA_BEER)) {
                mBeer = getIntent().getParcelableExtra(BeersActivity.EXTRA_BEER);
            }
        }

        if (mBeer == null) {
            return;
        }

        initBeerUI();

        initBreweryUI();

        mActionBar = getSupportActionBar();
        getSupportActionBar().setTitle(mBeer.getName() + " Beer");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialises the beer UI.
     */
    private void initBeerUI() {
        mTextViewTitle = (TextView) findViewById(R.id.textViewBeerTitle);

        if (mTextViewTitle != null) {
            mTextViewTitle.setText(mBeer.getName());
        }

        TextView textViewDescription = (TextView) findViewById(R.id.textViewBeerDescription);

        if (textViewDescription != null) {
            textViewDescription.setText(mBeer.getDescription());
        }

        mImageViewLabel = (ImageView) findViewById(R.id.imageViewBeerLabel);

        if (mImageViewLabel != null && mBeer.hasLabel()) {
            Picasso.with(this).load(mBeer.getLabels().getMedium())
                    .placeholder(R.drawable.beer_placeholder).into(mImageViewLabel,
                    new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            getColorFromImage();
                        }
                    });
        }
    }

    /**
     * Initialises the brewery UI.
     */
    private void initBreweryUI() {
        if (!mBeer.hasBrewery()) {
            return;
        }

        Brewery brewery = mBeer.getBrewery();

        TextView textViewBreweryTitle = (TextView) findViewById(R.id.textViewBreweryTitle);

        if (textViewBreweryTitle != null && brewery.getName() != null) {
            textViewBreweryTitle.setText(brewery.getName());
        }

        TextView textViewBreweryWebsite = (TextView) findViewById(R.id.textViewBreweryWebsite);

        if (textViewBreweryWebsite != null && brewery.getWebsite() != null) {
            textViewBreweryWebsite.setText(brewery.getWebsite());
        }

        TextView textViewBreweryDescription = (TextView)
                findViewById(R.id.textViewBreweryDescription);

        if (textViewBreweryDescription != null && brewery.getDescription() != null) {
            textViewBreweryDescription.setText(brewery.getDescription());

        }

        ImageView imageViewBreweryImage = (ImageView) findViewById(R.id.imageViewBreweryLabel);

        if (imageViewBreweryImage != null && brewery.hasImage()) {
            Picasso.with(this).load(brewery.getImage())
                    .placeholder(R.drawable.brewery_placeholder).into(imageViewBreweryImage);
        }
    }

    /**
     * Gets the color from the image to use as the action bar color.
     */
    private void getColorFromImage() {
        Bitmap bitmap = ((BitmapDrawable) mImageViewLabel.getDrawable()).getBitmap();

//        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//
//                Palette.Swatch swatch = palette.getVibrantSwatch();
//                Palette.Swatch swatchDark = palette.getDarkVibrantSwatch();
//
//                if (swatch != null) {
//                    mActionBar.setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));
//                    mTextViewTitle.setTextColor(swatch.getTitleTextColor());
//                }
//
//                if (swatchDark != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(swatchDark.getRgb());
//                    }
//                }
//            }
//        });
    }

    /**
     * Show the about dialog.
     */
    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_dialog);
        dialog.show();
    }
}