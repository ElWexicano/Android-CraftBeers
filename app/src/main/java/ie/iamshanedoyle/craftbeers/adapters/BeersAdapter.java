package ie.iamshanedoyle.craftbeers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.models.Beer;

/**
 * This adapter is used for display beers.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.ViewHolder> {

    private List<Beer> mBeers;
    private WeakReference<Context> mContextReference;

    public BeersAdapter(Context context, List<Beer> beers) {
        mBeers = beers;
        mContextReference = new WeakReference<>(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_beer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Beer beer = mBeers.get(i);

        viewHolder.textViewTitle.setText(beer.getName());

        if (beer.getDescription() == null || beer.getDescription().isEmpty()) {
            viewHolder.textViewDescription.setText("Description unavailable.");
        } else {
            viewHolder.textViewDescription.setText(beer.getDescription());
        }

        if (beer.hasBrewery()) {

            String brewedYear = "";

            if (beer.getYear() != 0) {
                brewedYear = "since " + beer.getYear();
            }

            String brewedBy = String.format(mContextReference.get().getString(R.string.brewed_by),
                    beer.getBrewery().getName(), brewedYear);
            CharSequence styledText = Html.fromHtml(brewedBy);
            viewHolder.textViewBreweryTitle.setText(styledText);
        } else {
            viewHolder.textViewBreweryTitle.setText("No Brewery Info Available");
        }

        if (beer.hasLabel()) {
            Picasso.with(mContextReference.get())
                    .load(beer.getLabels().getMedium())
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.imageViewLabel);
        } else {
            viewHolder.imageViewLabel.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return mBeers.size();
    }

    /**
     * Updates the beers in the adapter.
     *
     * @param beers A List of Beer objects.
     */
    public void updateBeers(List<Beer> beers) {
        mBeers = beers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewBreweryTitle;
        ImageView imageViewLabel;

        public ViewHolder(View v) {
            super(v);

            textViewTitle = (TextView) v.findViewById(R.id.textViewBeerTitle);
            textViewDescription = (TextView) v.findViewById(R.id.textViewBeerDescription);
            textViewBreweryTitle = (TextView) v.findViewById(R.id.textViewBreweryTitle);
            imageViewLabel = (ImageView) v.findViewById(R.id.imageViewBeerLabel);
        }
    }
}