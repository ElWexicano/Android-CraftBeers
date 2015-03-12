package ie.iamshanedoyle.craftbeers.events;

import ie.iamshanedoyle.craftbeers.models.Beer;

/**
 * A get beer event.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class GetBeerEvent extends Event {

    private Beer mBeer;

    public Beer getBeer() {
        return mBeer;
    }

    public void setBeer(Beer beer) {
        this.mBeer = beer;
    }

}
