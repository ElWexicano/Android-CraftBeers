package ie.iamshanedoyle.craftbeers.api;

import java.util.List;
import java.util.Map;

import ie.iamshanedoyle.craftbeers.models.Beer;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * This contains all the api endpoints for beers.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public interface BeersInterface {

    static final String URL_GET_BEERS = "/beers/";
    static final String URL_GET_BEER = "/beers/{beerId}/";
    static final String URL_SEARCH_BEERS = "/search/";

    static final String PATH_BEER_ID = "beerId";

    @GET(URL_GET_BEERS)
    BeersResponse getBeers(@QueryMap Map<String, String> parameters);

    @GET(URL_GET_BEER)
    BeerResponse getBeer(@Path(PATH_BEER_ID) String beerId, @Query("key") String key);

    @GET(URL_SEARCH_BEERS)
    BeersResponse searchBeers(@QueryMap Map<String, String> parameters);

    /**
     * A simple beer response.
     */
    public class BeerResponse {

        protected String status;
        protected String message;
        protected Beer data;

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Beer getData() {
            return data;
        }
    }

    /**
     * A simple Beers response.
     */
    public class BeersResponse {
        protected String status;
        protected List<Beer> data;
        protected int numberOfPages;

        public String getStatus() {
            return status;
        }

        public List<Beer> getData() {
            return data;
        }

        public int getNumberOfPages() {
            return numberOfPages;
        }
    }

}
