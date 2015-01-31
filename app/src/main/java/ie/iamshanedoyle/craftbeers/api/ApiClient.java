package ie.iamshanedoyle.craftbeers.api;

import android.content.Context;

import retrofit.RestAdapter;

/**
 * The ApiClient.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class ApiClient {

    private static RestAdapter sRestAdapter;

    private static final String BASE_URL = "http://api.brewerydb.com/v2";

    public static RestAdapter getApi(final Context context) {

        if (sRestAdapter == null) {
            sRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .build();

        }

        return sRestAdapter;
    }

}
