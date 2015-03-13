package ie.iamshanedoyle.craftbeers.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import ie.iamshanedoyle.craftbeers.CraftBeersApplication;
import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.adapters.BeersAdapter;
import ie.iamshanedoyle.craftbeers.adapters.RecyclerItemClickListener;
import ie.iamshanedoyle.craftbeers.events.GetBeersEvent;
import ie.iamshanedoyle.craftbeers.models.Beer;
import ie.iamshanedoyle.craftbeers.services.BeersService;
import io.fabric.sdk.android.Fabric;

/**
 * This activity is used for displaying a list of beers.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeersActivity extends ActionBarActivity {

    /**
     * Constants.
     */
    public static final String EXTRA_BEER = "ExtraBeer";
    private static final String SCREEN_NAME = "BeersScreen";
    private static final String EXTRA_BEERS = "ExtraBeers";
    private static final String EXTRA_KEYWORDS = "ExtraKeywords";
    private static final String EXTRA_PAGE_NUMBER = "ExtraPageNumber";
    private static final String EXTRA_NUMBER_OF_PAGES = "ExtraNumberOfPages";
    private static final String GMS_SEARCH_ACTION = "com.google.android.gms.actions.SEARCH_ACTION";

    /**
     * Mutables.
     */
    private List<Beer> mBeers = new ArrayList<>();
    private String mBeerKeywords = "";
    private int mPageNumber = 1;
    private int mNumberOfPages;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsSearching;
    private BeersAdapter mBeersAdapter;

    /**
     * Views
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers);

        Fabric.with(this, new Crashlytics());

        // Google Analytics Screen Tagging.
        Tracker t = ((CraftBeersApplication) getApplication()).getTracker();
        t.setScreenName(SCREEN_NAME);
        t.send(new HitBuilders.ScreenViewBuilder().build());

        String action = getIntent().getAction();
        if (action.equals(Intent.ACTION_SEARCH) ||
                action.equals(GMS_SEARCH_ACTION)) {
            mBeerKeywords = getIntent().getStringExtra(SearchManager.QUERY);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        mBeersAdapter = new BeersAdapter(this, mBeers);
        recyclerView.setAdapter(mBeersAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                onBeerClicked(position);
                            }
                        }));

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLayoutManager != null) {
                    int firstVisiblePosition =
                            mLayoutManager.findFirstCompletelyVisibleItemPosition();

                    mSwipeRefreshLayout.setEnabled(firstVisiblePosition == 0);

                    int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();

                    if (lastVisiblePosition >= (mBeers.size() - 4) && !mIsSearching) {

                        if (mPageNumber < mNumberOfPages) {
                            performInfiniteScrolling();
                        } else {
                            showNoMoreBeers();
                        }

                    }
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.beer_red, R.color.beer_dark_red,
                R.color.beer_red, R.color.beer_dark_red);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBeers();
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        if (mBeers.size() == 0) {
            search();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mBeers != null) {
            outState.putParcelableArrayList(EXTRA_BEERS, new ArrayList<> (mBeers));
        }

        outState.putString(EXTRA_KEYWORDS, mBeerKeywords);
        outState.putInt(EXTRA_PAGE_NUMBER, mPageNumber);
        outState.putInt(EXTRA_NUMBER_OF_PAGES, mNumberOfPages);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(EXTRA_BEERS)) {
            mBeers = savedInstanceState.getParcelableArrayList(EXTRA_BEERS);
            mBeersAdapter.updateBeers(mBeers);
        }

        mBeerKeywords = savedInstanceState.getString(EXTRA_KEYWORDS);
        mPageNumber = savedInstanceState.getInt(EXTRA_PAGE_NUMBER, 1);
        mNumberOfPages = savedInstanceState.getInt(EXTRA_NUMBER_OF_PAGES, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beers, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                search.clearFocus();
                performKeywordSearch(s);
                return true;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                performKeywordSearch("");
                return false;
            }
        });

        if (mBeerKeywords != null && !mBeerKeywords.isEmpty()) {
            search.setQuery(mBeerKeywords, false);
        }

        return true;
    }

    /**
     * Called when we receive a GetBeersEvent.
     *
     * @param getBeersEvent A GetBeersEvent object. Contains a list of beers.
     */
    @SuppressWarnings("unused")
    public void onEventMainThread(GetBeersEvent getBeersEvent) {
        updateBeers(getBeersEvent.getBeers());
        mNumberOfPages = getBeersEvent.getNumberOfPages();
        mIsSearching = false;
    }

    /**
     * Refreshes the list of beers.
     */
    private void refreshBeers() {
        performKeywordSearch(mBeerKeywords);
    }

    /**
     * Performs keyword search.
     *
     * @param keywordSearch A String of the keyword search.
     */
    private void performKeywordSearch(String keywordSearch) {
        mBeers.clear();
        mBeerKeywords = keywordSearch;
        mPageNumber = 1;
        search();
    }

    /**
     * Performs infinite scrolling.
     */
    private void performInfiniteScrolling() {
        mPageNumber++;
        search();
    }

    /**
     * Performs a search.
     */
    private void search() {
        mSwipeRefreshLayout.setRefreshing(true);
        mIsSearching = true;
        BeersService.startActionGetBeers(this, mBeerKeywords, mPageNumber);
    }

    /**
     * Updates beers.
     *
     * @param beers A List of Beer objects.
     */
    private void updateBeers(List<Beer> beers) {
        if (beers != null) {
            mBeers.addAll(beers);
            mBeersAdapter.notifyDataSetChanged();
            hideEmptyView();
        } else {
            if (mBeers == null || mBeers.isEmpty()) {
                showEmptyView();
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);
    }

    /**
     * Shows the empty view.
     */
    private void showEmptyView() {
        findViewById(R.id.imageViewEmpty).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewEmptyTitle).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewEmptyHint).setVisibility(View.VISIBLE);
    }

    /**
     * Hides the empty view.
     */
    private void hideEmptyView() {
        findViewById(R.id.imageViewEmpty).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewEmptyTitle).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewEmptyHint).setVisibility(View.INVISIBLE);
    }

    /**
     * Shows a no more beers toast.
     */
    private void showNoMoreBeers() {
        Toast.makeText(this, "No More Beers", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles an intent.
     *
     * @param intent A Intent.
     */
    private void handleIntent(Intent intent) {
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mBeerKeywords = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, mBeerKeywords, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * On beer clicked.
     *
     * @param position int Position of the beer that was clicked.
     */
    private void onBeerClicked(int position) {
        Beer beer = mBeers.get(position);
        Intent intent = new Intent(this, BeerViewActivity.class);
        intent.putExtra(EXTRA_BEER, beer);
        startActivity(intent);
    }
}
