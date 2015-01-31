package ie.iamshanedoyle.craftbeers.events;

import ie.iamshanedoyle.craftbeers.models.Beer;

/**
 * A get beer event.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class GetBeerEvent {

    private String mStatus;
    private Beer mBeer;

    public void setBeer(Beer beer) {
        this.mBeer = beer;
    }

    public Beer getBeer() {
        return mBeer;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getStatus() {
        return mStatus;
    }
}
