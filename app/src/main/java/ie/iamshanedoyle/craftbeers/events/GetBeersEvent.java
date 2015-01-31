package ie.iamshanedoyle.craftbeers.events;

import java.util.ArrayList;
import java.util.List;

import ie.iamshanedoyle.craftbeers.models.Beer;

/**
 * A get beers event.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class GetBeersEvent {

    private String mStatus;
    private List<Beer> mBeers = new ArrayList<Beer>();

    public List<Beer> getBeers() {
        return mBeers;
    }

    public void setBeers(List<Beer> beers) {
        this.mBeers = beers;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getStatus() {
        return mStatus;
    }
}
