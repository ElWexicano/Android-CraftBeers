package ie.iamshanedoyle.craftbeers.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.squareup.picasso.Picasso;

import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.models.Beer;
import ie.iamshanedoyle.craftbeers.models.Brewery;
import ie.iamshanedoyle.craftbeers.utils.GeneralUtils;

/**
 * This activity is used for displaying information about a beer.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeerViewActivity extends BaseActivity {

    /**
     * Constants
     */
    private static final String SCREEN_NAME = "view_beer";
    private static final String BEER = "Beer";

    /**
     * Mutables.
     */
    private Beer mBeer;

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
        } else {
            trackBeerContentView();
        }

        if (savedInstanceState != null) {
            mBeer = savedInstanceState.getParcelable(BeersActivity.EXTRA_BEER);
        }

        initActionBar();
        initBeerUI();
        initBreweryUI();
    }

    /**
     * Tracks a beer content view for Answers.
     */
    private void trackBeerContentView() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(mBeer.getName())
                .putContentType(BEER)
                .putContentId(mBeer.getId()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BeersActivity.EXTRA_BEER, mBeer);

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
    protected Bundle getScreenBundle() {

        Bundle params = new Bundle();

        if (mBeer != null) {
            params.putString("beer_name", mBeer.getName());
            params.putString("beer_id", mBeer.getId());
        }

        return params;
    }

    /**
     * Initialises the Action Bar.
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.fadingToolbar);
        setSupportActionBar(toolbar);

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