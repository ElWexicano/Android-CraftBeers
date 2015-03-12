package ie.iamshanedoyle.craftbeers.events;

import java.util.ArrayList;
import java.util.List;

import ie.iamshanedoyle.craftbeers.models.Beer;

/**
 * A get beers event.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class GetBeersEvent extends Event {

    private List<Beer> mBeers = new ArrayList<Beer>();
    private int mNumberOfPages;

    public List<Beer> getBeers() {
        return mBeers;
    }

    public void setBeers(List<Beer> beers) {
        this.mBeers = beers;
    }

    public int getNumberOfPages() {
        return mNumberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.mNumberOfPages = numberOfPages;
    }
}
