package ie.iamshanedoyle.craftbeers.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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

    /**
     * Constants
     */
    private static final String SCREEN_NAME = "BeerViewScreen";
    private static final String EXTRA_LAST_RATIO = "LastRatio";
    private static final String EXTRA_LAST_ALPHA = "LastAlpha";
    private static final String EXTRA_LAST_DAMPED_SCROLL = "LastDampedScroll";

    /**
     * Mutables.
     */
    private Beer mBeer;
    private int mLastAlpha = 0;
    private float mLastRatio;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private Drawable mActionBarBackgroundDrawable;

    /**
     * Views
     */
    private Toolbar mToolbar;
    private View mHeader;

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

        if (savedInstanceState != null) {
            mBeer = savedInstanceState.getParcelable(BeersActivity.EXTRA_BEER);
            mLastAlpha = savedInstanceState.getInt(EXTRA_LAST_ALPHA);
            mLastRatio = savedInstanceState.getFloat(EXTRA_LAST_RATIO);
            mLastDampedScroll = savedInstanceState.getInt(EXTRA_LAST_DAMPED_SCROLL);
        }

        initActionBar();
        initAnimations();
        initBeerUI();
        initBreweryUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BeersActivity.EXTRA_BEER, mBeer);
        outState.putInt(EXTRA_LAST_ALPHA, mLastAlpha);
        outState.putFloat(EXTRA_LAST_RATIO, mLastRatio);
        outState.putInt(EXTRA_LAST_DAMPED_SCROLL, mLastDampedScroll);

        super.onSaveInstanceState(outState);
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
    public void onScrollChanged(int t) {
        final int headerHeight = mHeader.getHeight() - mToolbar.getHeight();
        final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
        final int alpha = (int) (ratio * 255);

        mLastAlpha = alpha;
        mLastRatio = ratio;

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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateStatusBarColor(float ratio) {
        if (CompatUtils.isLollipopOrAbove()) {
            int r = GeneralUtils.interpolate(Color.red(mInitialStatusBarColor),
                    Color.red(mFinalStatusBarColor), 1 - ratio);
            int g = GeneralUtils.interpolate(Color.green(mInitialStatusBarColor),
                    Color.green(mFinalStatusBarColor), 1 - ratio);
            int b = GeneralUtils.interpolate(Color.blue(mInitialStatusBarColor),
                    Color.blue(mFinalStatusBarColor), 1 - ratio);
            getWindow().setStatusBarColor(Color.rgb(r, g, b));
        }
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
        mInitialStatusBarColor = Color.TRANSPARENT;
        mFinalStatusBarColor = getResources().getColor(R.color.beer_dark_red);

        mHeader = findViewById(R.id.header);

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollView.addCallbacks(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mBeer.getName() + " " + getString(R.string.beer));
        }

        updateActionBarColor(mLastAlpha);
        updateStatusBarColor(mLastRatio);
        updateParallaxEffect(0);
    }

    /**
     * Initialises the animations.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initAnimations() {
        if (CompatUtils.isLollipopOrAbove()) {
            // Enter Transition
            Slide slideEnterTransition = new Slide(Gravity.BOTTOM);
            slideEnterTransition.excludeTarget(android.R.id.navigationBarBackground, true);
            slideEnterTransition.excludeTarget(android.R.id.statusBarBackground, true);
            slideEnterTransition.excludeTarget(android.R.id.background, true);
            slideEnterTransition.excludeTarget(R.id.toolbar, true);
            slideEnterTransition.excludeTarget(R.id.header, true);
            // Exit Transition
            Slide slideExitTransition = new Slide(Gravity.BOTTOM);
            slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
            slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
            slideExitTransition.excludeTarget(android.R.id.background, true);
            slideExitTransition.excludeTarget(R.id.toolbar, true);
            slideExitTransition.excludeTarget(R.id.header, true);

            getWindow().setEnterTransition(slideEnterTransition);
            getWindow().setEnterTransition(slideExitTransition);
        }
    }

    /**
     * Initialises the beer UI.
     */
    private void initBeerUI() {
        if (mBeer == null) {
            return;
        }

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
                    .placeholder(R.drawable.img_header).into(imageViewHeader);
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
        if (mBeer == null || !mBeer.hasBrewery()) {
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
            textViewBreweryWebsite.setPaintFlags(textViewBreweryWebsite.getPaintFlags() |
                    Paint.UNDERLINE_TEXT_FLAG);
            textViewBreweryWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBreweryWebsite();
                }
            });
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
     * Opens the Google Play Store to check for updates.
     */
    private void openGooglePlayPage() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Opens the brewery website.
     */
    private void openBreweryWebsite() {
        Intent intent = new Intent(this, BreweryWebsiteActivity.class);
        intent.putExtra(BreweryWebsiteActivity.EXTRA_BREWERY_URL, mBeer.getBrewery().getWebsite());
        startActivity(intent);
    }

    /**
     * Show the about dialog.
     */
    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_dialog);

        TextView textViewVersion = (TextView) dialog.findViewById(R.id.textViewVersion);

        String version = String.format(getString(R.string.version),
                GeneralUtils.getApplicationVersion(this));
        String tapToUpdate = getString(R.string.tap_to_update);

        SpannableString spannableString = new SpannableString(version + "\n" + tapToUpdate);

        int start = spannableString.length() - tapToUpdate.length();
        int end = spannableString.length();

        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), "Version".length(),
                version.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.AboutSubText), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (textViewVersion != null) {
            textViewVersion.setText(spannableString);
            textViewVersion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGooglePlayPage();
                }
            });
        }

        dialog.show();
    }
}