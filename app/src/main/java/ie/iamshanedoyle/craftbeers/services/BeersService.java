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
import ie.iamshanedoyle.craftbeers.events.GetBeerEvent;
import ie.iamshanedoyle.craftbeers.events.GetBeersEvent;
import retrofit.RetrofitError;

/**
 * This service is used to get beers.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeersService extends IntentService{

    public static final String ACTION_GET_BEERS = "ActionGetBeers";
    public static final String ACTION_GET_BEER = "ActionGetBeer";

    public static final String EXTRA_BEER_KEYWORD = "ExtraBeerKeyword";
    public static final String EXTRA_BEER_ID = "ExtraBeerId";

    public static final String NAME = "BeersService";

    public BeersService() {
        super(NAME);
        setIntentRedelivery(false);
    }

    /**
     * Starts the BeersService with GetBeers action.
     *
     * @param context A context.
     * @param beerKeyword A String of the beer keywords.
     */
    public static void startActionGetBeers(Context context, String beerKeyword) {
        Intent intent = new Intent(context, BeersService.class);
        intent.setAction(ACTION_GET_BEERS);
        intent.putExtra(EXTRA_BEER_KEYWORD, beerKeyword);
        context.startService(intent);
    }

    /**
     * Starts the BeersService with GetBeer action.
     *
     * @param context A context.
     * @param beerId A String of the beer id.
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

        final String action = intent.getAction();

        if (ACTION_GET_BEERS.equals(action)) {
            final String keyword = intent.getStringExtra(EXTRA_BEER_KEYWORD);
            getBeers(keyword);
        } else if (ACTION_GET_BEER.equals(action)) {
            final String beerId = intent.getStringExtra(EXTRA_BEER_ID);
            getBeer(beerId);
        }
    }

    /**
     * Used to get a beer. Will post a GetBeersEvent.
     *
     * @param keyword A String of the beer keyword.
     */
    private void getBeers(String keyword) {
        GetBeersEvent getBeersEvent = new GetBeersEvent();

        String apiKey = getString(R.string.beer_api);

        Map<String, String> parametersMap = new HashMap<String, String>();

        parametersMap.put("key", apiKey);
        parametersMap.put("abv", "-10");

        if (keyword != null && !keyword.equals("")) {
            parametersMap.put("name", keyword);
        }

        try {
            BeersInterface.BeersResponse beersResponse =
                    ApiClient.getApi(this).create(BeersInterface.class).getBeers(parametersMap);
            getBeersEvent.setBeers(beersResponse.getData());
            getBeersEvent.setStatus(beersResponse.getStatus());
            EventBus.getDefault().post(getBeersEvent);
        } catch (RetrofitError retrofitError) {
            getBeersEvent.setStatus("failure");
        }

    }

    /**
     * Used to get a beer. Will post a GetBeerEvent.
     *
     * @param beerId A String of the beer id.
     */
    private void getBeer(String beerId) {
        GetBeerEvent getBeerEvent = new GetBeerEvent();

        String apiKey = getString(R.string.beer_api);

        try {
            BeersInterface.BeerResponse beerResponse =
                    ApiClient.getApi(this).create(BeersInterface.class).getBeer(beerId, apiKey);
            getBeerEvent.setBeer(beerResponse.getData());
            getBeerEvent.setStatus(beerResponse.getStatus());
            EventBus.getDefault().post(getBeerEvent);
        } catch (RetrofitError retrofitError) {
            getBeerEvent.setStatus("failure");
        }

    }
}
