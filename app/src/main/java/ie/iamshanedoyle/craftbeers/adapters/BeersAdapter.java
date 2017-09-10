package ie.iamshanedoyle.craftbeers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
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
            viewHolder.textViewDescription.setText(
                    mContextReference.get().getString(R.string.description_unavailable));
        } else {
            viewHolder.textViewDescription.setText(beer.getDescription());
        }

        setAttributeText(viewHolder.textViewStyle,
                mContextReference.get().getString(R.string.style), beer.getStyleAsString());

        setAttributeText(viewHolder.textViewAbv,
                mContextReference.get().getString(R.string.abv), beer.getAbvAsString());

        setAttributeText(viewHolder.textViewIbu,
                mContextReference.get().getString(R.string.ibu), beer.getIbuAsString());

        setAttributeText(viewHolder.textViewGlass,
                mContextReference.get().getString(R.string.glass), beer.getGlassAsString());

        setAttributeText(viewHolder.textViewYear,
                mContextReference.get().getString(R.string.year), beer.getYearAsString());

        if (beer.hasLabel()) {
            Picasso.with(mContextReference.get())
                    .load(beer.getLabels().getMedium())
                    .placeholder(R.drawable.beer_placeholder)
                    .into(viewHolder.imageViewLabel);
        } else {
            viewHolder.imageViewLabel.setImageResource(R.drawable.beer_placeholder);
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
        TextView textViewAbv;
        TextView textViewIbu;
        TextView textViewGlass;
        TextView textViewYear;
        TextView textViewStyle;
        ImageView imageViewLabel;

        public ViewHolder(View v) {
            super(v);

            textViewTitle = (TextView) v.findViewById(R.id.textViewBeerTitle);
            textViewDescription = (TextView) v.findViewById(R.id.textViewBeerDescription);
            textViewAbv = (TextView) v.findViewById(R.id.textViewAbv);
            textViewIbu = (TextView) v.findViewById(R.id.textViewIbu);
            textViewGlass = (TextView) v.findViewById(R.id.textViewGlass);
            textViewYear = (TextView) v.findViewById(R.id.textViewYear);
            textViewStyle = (TextView) v.findViewById(R.id.textViewStyle);
            imageViewLabel = (ImageView) v.findViewById(R.id.imageViewBeerLabel);

        }
    }

    /**
     * Sets an attribute text value.
     *
     * @param textView The TextView to update.
     * @param label    A String of the attribute label.
     * @param value    A String of the attribute value.
     */
    private void setAttributeText(TextView textView, String label, String value) {
        SpannableString spannableString = new SpannableString(label + "\n" + value);
        spannableString.setSpan(new TextAppearanceSpan(mContextReference.get(), R.style.BeerValue),
                label.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }
}