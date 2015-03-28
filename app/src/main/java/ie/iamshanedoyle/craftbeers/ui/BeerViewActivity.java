package ie.iamshanedoyle.craftbeers.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.models.Beer;
import ie.iamshanedoyle.craftbeers.models.Brewery;
import ie.iamshanedoyle.craftbeers.ui.widgets.ObservableScrollView;
import ie.iamshanedoyle.craftbeers.utils.CompatUtils;
import ie.iamshanedoyle.craftbeers.utils.GeneralUtils;

/**
 * This activity is used for displaying information about a beer.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeerViewActivity extends BaseActivity implements ObservableScrollView.ScrollListener {

    private static final String SCREEN_NAME = "BeerViewScreen";
    private Beer mBeer;

    // Header stuff
    private Toolbar mToolbar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private SystemBarTintManager mStatusBarManager;

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

        initActionBar();
        initBeerUI();
        initBreweryUI();
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

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onScrollChanged(int l, int t) {
        final int headerHeight = mHeader.getHeight() - mToolbar.getHeight();
        final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
        final int alpha = (int) (ratio * 255);

        updateActionBarColor(alpha);
        updateStatusBarColor(ratio);
        updateParallaxEffect(t);
    }

    /**
     * Updates the action bar background color.
     *
     * @param alpha A int of the alpha.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateActionBarColor(int alpha) {
        mActionBarBackgroundDrawable.setAlpha(alpha);

        if (CompatUtils.isJellyBeanOrAbove()) {
            mToolbar.setBackground(mActionBarBackgroundDrawable);
        } else {
            mToolbar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        }
    }

    /**
     * Updates the status bar color.
     *
     * @param ratio A int of the ratio.
     */
    private void updateStatusBarColor(float ratio) {
        int r = GeneralUtils.interpolate(Color.red(mInitialStatusBarColor),
                Color.red(mFinalStatusBarColor), 1 - ratio);
        int g = GeneralUtils.interpolate(Color.green(mInitialStatusBarColor),
                Color.green(mFinalStatusBarColor), 1 - ratio);
        int b = GeneralUtils.interpolate(Color.blue(mInitialStatusBarColor),
                Color.blue(mFinalStatusBarColor), 1 - ratio);
        mStatusBarManager.setTintColor(Color.rgb(r, g, b));
    }

    /**
     * Updates the parallax effect.
     *
     * @param scrollPosition A int of the scroll position.
     */
    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);
        mLastDampedScroll = dampedScroll;
    }

    /**
     * Initialises the Action Bar.
     */
    private void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBarBackgroundDrawable = mToolbar.getBackground();

        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);
        mInitialStatusBarColor = Color.TRANSPARENT;
        mFinalStatusBarColor = getResources().getColor(R.color.beer_dark_red);

        mHeader = findViewById(R.id.header);

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollView.addCallbacks(this);

        onScrollChanged(-1, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mBeer.getName() + " " + getString(R.string.beer));
        }
    }

    /**
     * Initialises the beer UI.
     */
    private void initBeerUI() {
        TextView mTextViewTitle = (TextView) findViewById(R.id.textViewBeerTitle);

        if (mTextViewTitle != null) {
            mTextViewTitle.setText(mBeer.getName());
        }

        TextView textViewDescription = (TextView) findViewById(R.id.textViewBeerDescription);

        String description = mBeer.getDescription();

        if (description == null) {
            description = getString(R.string.description_unavailable);
        }

        textViewDescription.setText(description);

        ImageView imageViewLabel = (ImageView) findViewById(R.id.imageViewBeerLabel);

        if (imageViewLabel != null && mBeer.hasLabel()) {
            Picasso.with(this).load(mBeer.getLabels().getMedium())
                    .placeholder(R.drawable.beer_placeholder).into(imageViewLabel);
        }

        ImageView imageViewHeader = (ImageView) findViewById(R.id.header);

        if (imageViewHeader != null && mBeer.hasLabel()) {
            Picasso.with(this).load(mBeer.getLabels().getLarge())
                    .placeholder(R.drawable.header).into(imageViewHeader);
        }

        TextView textViewStyle = (TextView) findViewById(R.id.textViewStyle);
        setAttributeText(textViewStyle, getString(R.string.style), mBeer.getStyleAsString());

        TextView textViewAbv = (TextView) findViewById(R.id.textViewAbv);
        setAttributeText(textViewAbv, getString(R.string.abv), mBeer.getAbvAsString());

        TextView textViewIbu = (TextView) findViewById(R.id.textViewIbu);
        setAttributeText(textViewIbu, getString(R.string.ibu), mBeer.getIbuAsString());

        TextView textViewGlass = (TextView) findViewById(R.id.textViewGlass);
        setAttributeText(textViewGlass, getString(R.string.glass), mBeer.getGlassAsString());

        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        setAttributeText(textViewYear, getString(R.string.year), mBeer.getYearAsString());
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
     * Sets an attribute text value.
     *
     * @param textView The TextView to update.
     * @param label    A String of the attribute label.
     * @param value    A String of the attribute value.
     */
    private void setAttributeText(TextView textView, String label, String value) {
        SpannableString spannableString = new SpannableString(label + "\n" + value);
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.BeerValue), label.length(),
                spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    /**
     * Show the about dialog.
     */
    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_dialog);

        TextView textView = (TextView) dialog.findViewById(R.id.textViewVersion);

        if (textView != null) {
            textView.setText(GeneralUtils.getApplicationVersion(this));
        }

        dialog.show();
    }
}