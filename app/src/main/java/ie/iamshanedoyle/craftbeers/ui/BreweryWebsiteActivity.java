package ie.iamshanedoyle.craftbeers.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import ie.iamshanedoyle.craftbeers.R;

/**
 * This activity is used for displaying the brewery website.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BreweryWebsiteActivity extends BaseActivity {

    public static final String EXTRA_BREWERY_URL = "ExtraBreweryUrl";
    /**
     * Constants.
     */
    private static final String SCREEN_NAME = "WebViewScreen";
    private static final String BREWERY_WEBSITE = "Brewery Website";
    private static final String BREWERY = "Brewery";

    /**
     * Mutables
     */
    private String mBreweryWebsiteUrl;

    /**
     * Views
     */
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewery_website);

        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_BREWERY_URL)) {
                mBreweryWebsiteUrl = getIntent().getStringExtra(EXTRA_BREWERY_URL);
                trackBreweryWebsiteContentView();
            }
        }

        initActionBar();
        initWebView();
    }

    /**
     * Tracks a Brewery Website Content View
     */
    private void trackBreweryWebsiteContentView() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(mBreweryWebsiteUrl)
                .putContentType(BREWERY_WEBSITE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_brewery_website, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                refreshWebView();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
    }

    /**
     * Initialises the ActionBar.
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Brewery Website");

        if (mBreweryWebsiteUrl != null) {
            toolbar.setSubtitle(mBreweryWebsiteUrl);
        }

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(10);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialises the WebView.
     */
    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if (mBreweryWebsiteUrl != null) {
            mWebView.loadUrl(mBreweryWebsiteUrl);
        }
    }

    /**
     * Refreshes the web view.
     */
    private void refreshWebView() {
        mWebView.reload();
    }
}