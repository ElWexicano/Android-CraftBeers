package ie.iamshanedoyle.craftbeers.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.api.ApiClient;
import ie.iamshanedoyle.craftbeers.api.BeersInterface;
import ie.iamshanedoyle.craftbeers.events.Event;
import ie.iamshanedoyle.craftbeers.events.GetBeerEvent;
import ie.iamshanedoyle.craftbeers.events.GetBeersEvent;
import retrofit.RetrofitError;

/**
 * This service is used to get beers.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeersService extends IntentService {

    public static final String ACTION_GET_BEERS = "ActionGetBeers";
    public static final String ACTION_GET_BEER = "ActionGetBeer";

    public static final String EXTRA_BEER_KEYWORD = "ExtraBeerKeyword";
    public static final String EXTRA_BEER_ID = "ExtraBeerId";
    public static final String EXTRA_PAGE = "ExtraPage";

    public static final String NAME = "BeersService";

    private String mApiKey;

    public BeersService() {
        super(NAME);
        setIntentRedelivery(false);
    }

    /**
     * Starts the BeersService with GetBeers action.
     *
     * @param context     A context.
     * @param beerKeyword A String of the beer keywords.
     * @param pageNumber  A int of the page number
     */
    public static void startActionGetBeers(Context context, String beerKeyword, int pageNumber) {
        Intent intent = new Intent(context, BeersService.class);
        intent.setAction(ACTION_GET_BEERS);
        intent.putExtra(EXTRA_BEER_KEYWORD, beerKeyword);
        intent.putExtra(EXTRA_PAGE, pageNumber);
        context.startService(intent);
    }

    /**
     * Starts the BeersService with GetBeer action.
     *
     * @param context A context.
     * @param beerId  A String of the beer id.
     */
    public static void startActionGetBeer(Context context, String beerId) {
        Intent intent = new Intent(context, BeersService.class);
        intent.setAction(ACTION_GET_BEER);
        intent.putExtra(EXTRA_BEER_ID, beerId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        mApiKey = getString(R.string.beer_api);

        final String action = intent.getAction();

        if (ACTION_GET_BEERS.equals(action)) {
            final String keyword = intent.getStringExtra(EXTRA_BEER_KEYWORD);
            final int page = intent.getIntExtra(EXTRA_PAGE, -1);

            if (keyword == null || keyword.isEmpty()) {
                getBeers(page);
            } else {
                searchBeers(keyword, page);
            }

        } else if (ACTION_GET_BEER.equals(action)) {
            final String beerId = intent.getStringExtra(EXTRA_BEER_ID);
            getBeer(beerId);
        }
    }

    /**
     * Used to get a beer. Will post a GetBeersEvent.
     *
     * @param pageNumber A int of the page number.
     */
    private void getBeers(int pageNumber) {
        GetBeersEvent getBeersEvent = new GetBeersEvent();

        Map<String, String> parametersMap = new HashMap<String, String>();

        if (pageNumber != -1) {
            parametersMap.put("p", Integer.toString(pageNumber));
        }

        parametersMap.put("key", mApiKey);
        parametersMap.put("abv", "-10");
        parametersMap.put("withBreweries", "Y");

        try {
            BeersInterface.BeersResponse beersResponse =
                    ApiClient.getApi(this).create(BeersInterface.class).getBeers(parametersMap);
            getBeersEvent.setBeers(beersResponse.getData());
            getBeersEvent.setStatus(Event.Status.SUCCESSFUL);
            getBeersEvent.setNumberOfPages(beersResponse.getNumberOfPages());
            EventBus.getDefault().post(getBeersEvent);
        } catch (RetrofitError retrofitError) {
            getBeersEvent.setStatus(Event.Status.FAILED);
        }

    }

    /**
     * Search for beers. Will post a GetBeersEvent.
     *
     * @param keyword    A String of keywords to use in the query.
     * @param pageNumber A int of the page number.
     */
    private void searchBeers(String keyword, int pageNumber) {
        GetBeersEvent getBeersEvent = new GetBeersEvent();

        Map<String, String> parametersMap = new HashMap<String, String>();

        if (pageNumber != -1) {
            parametersMap.put("p", Integer.toString(pageNumber));
        }

        parametersMap.put("key", mApiKey);
        parametersMap.put("q", keyword);
        parametersMap.put("type", "beer");
        parametersMap.put("withBreweries", "Y");

        try {
            BeersInterface.BeersResponse beersResponse =
                    ApiClient.getApi(this).create(BeersInterface.class).searchBeers(parametersMap);
            getBeersEvent.setBeers(beersResponse.getData());
            getBeersEvent.setStatus(Event.Status.SUCCESSFUL);
            getBeersEvent.setNumberOfPages(beersResponse.getNumberOfPages());
            EventBus.getDefault().post(getBeersEvent);
        } catch (RetrofitError retrofitError) {
            getBeersEvent.setStatus(Event.Status.FAILED);
        }
    }


    /**
     * Used to get a beer. Will post a GetBeerEvent.
     *
     * @param beerId A String of the beer id.
     */
    private void getBeer(String beerId) {
        GetBeerEvent getBeerEvent = new GetBeerEvent();

        try {
            BeersInterface.BeerResponse beerResponse =
                    ApiClient.getApi(this).create(BeersInterface.class).getBeer(beerId, mApiKey);
            getBeerEvent.setBeer(beerResponse.getData());
            getBeerEvent.setStatus(Event.Status.SUCCESSFUL);
            EventBus.getDefault().post(getBeerEvent);
        } catch (RetrofitError retrofitError) {
            getBeerEvent.setStatus(Event.Status.FAILED);
        }
    }
}
