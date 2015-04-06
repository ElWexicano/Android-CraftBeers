package ie.iamshanedoyle.craftbeers.api;

import retrofit.RestAdapter;

/**
 * The ApiClient.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class ApiClient {

    private static final String BASE_URL = "http://api.brewerydb.com/v2";
    private static RestAdapter sRestAdapter;

    /**
     * Gets the API (RestAdapter) used to perform requests to the BreweryDB API.
     *
     * @return A RestAdapter.
     */
    public static RestAdapter getApi() {

        if (sRestAdapter == null) {
            sRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .build();

        }

        return sRestAdapter;
    }

}
