package ie.iamshanedoyle.craftbeers.ui;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private boolean mIsShowingMore = false;

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

        String description = mBeer.getDescription();

        if (description == null) {
            description = "Description Unavailable.";
        }

        textViewDescription.setText(description);
        TextView textViewShowMore = (TextView) findViewById(R.id.textViewShowMore);

        textViewShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsShowingMore) {
                    hideMoreDescription();
                } else {
                    showMoreDescription();
                }
            }
        });

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

        TextView textViewStyle = (TextView) findViewById(R.id.textViewStyle);
        setAttributeText(textViewStyle, "STYLE", mBeer.getStyleAsString());

        TextView textViewAbv = (TextView) findViewById(R.id.textViewAbv);
        setAttributeText(textViewAbv, "ABV", mBeer.getAbvAsString());

        TextView textViewIbu = (TextView) findViewById(R.id.textViewIbu);
        setAttributeText(textViewIbu, "IBU", mBeer.getIbuAsString());

        TextView textViewGlass = (TextView) findViewById(R.id.textViewGlass);
        setAttributeText(textViewGlass, "GLASS", mBeer.getGlassAsString());

        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        setAttributeText(textViewYear, "YEAR", mBeer.getYearAsString());
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

    private void setAttributeText(TextView textView, String label, String value) {
        SpannableString spannableString = new SpannableString(label + "\n" + value);
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.BeerValue), label.length(),
                spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
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
     * Shows more of the description.
     */
    private void showMoreDescription() {
        TextView textViewDescription = (TextView) findViewById(R.id.textViewBeerDescription);
        TextView textViewShowMore = (TextView) findViewById(R.id.textViewShowMore);

        textViewShowMore.setText("SHOW LESS");
        mIsShowingMore = true;

        ObjectAnimator animation = ObjectAnimator.ofInt(
                textViewDescription,
                "maxLines",
                100);
        animation.setDuration(500);
        animation.start();
    }

    /**
     * Hides more of the description.
     */
    private void hideMoreDescription() {
        TextView textViewDescription = (TextView) findViewById(R.id.textViewBeerDescription);
        TextView textViewShowMore = (TextView) findViewById(R.id.textViewShowMore);

        textViewShowMore.setText("SHOW MORE");
        mIsShowingMore = false;

        ObjectAnimator animation = ObjectAnimator.ofInt(
                textViewDescription,
                "maxLines",
                5);
        animation.setDuration(500);
        animation.start();
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