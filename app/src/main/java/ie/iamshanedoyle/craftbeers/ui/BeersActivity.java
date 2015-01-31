package ie.iamshanedoyle.craftbeers.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.adapters.BeersAdapter;
import ie.iamshanedoyle.craftbeers.adapters.RecyclerItemClickListener;
import ie.iamshanedoyle.craftbeers.events.GetBeersEvent;
import ie.iamshanedoyle.craftbeers.models.Beer;
import ie.iamshanedoyle.craftbeers.services.BeersService;

/**
 * This activity is used for displaying a list of beers.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeersActivity extends ActionBarActivity {

    public static final String EXTRA_BEER = "ExtraBeer";
    public static final String EXTRA_BEERS = "ExtraBeers";

    private List<Beer> mBeers = new ArrayList<>();
    private String mBeerKeywords = "";

    private BeersAdapter mBeersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        if (mBeers.size() == 0) {
            BeersService.startActionGetBeers(this, mBeerKeywords);
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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(EXTRA_BEERS)) {
            mBeers = savedInstanceState.getParcelableArrayList(EXTRA_BEERS);
            updateBeers(mBeers);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beers, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                search.clearFocus();
                performSearch(s);
                return true;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                performSearch("");
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(GetBeersEvent getBeersEvent) {
        updateBeers(getBeersEvent.getBeers());
    }

    private void performSearch(String keywordSearch) {
        mBeers.clear();
        mBeerKeywords = keywordSearch;
        BeersService.startActionGetBeers(this, mBeerKeywords);
    }

    private void updateBeers(List<Beer> beers) {
        if (beers != null) {
            mBeers.addAll(beers);
            mBeersAdapter.notifyDataSetChanged();
        }
    }

    private void handleIntent(Intent intent) {
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mBeerKeywords = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, mBeerKeywords, Toast.LENGTH_SHORT).show();
        }
    }

    private void onBeerClicked(int position) {
        Beer beer = mBeers.get(position);
        Intent intent = new Intent(this, BeerViewActivity.class);
        intent.putExtra(EXTRA_BEER, beer);

        startActivity(intent);
    }
}
