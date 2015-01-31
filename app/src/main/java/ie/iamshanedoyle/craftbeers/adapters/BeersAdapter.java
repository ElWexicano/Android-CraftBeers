package ie.iamshanedoyle.craftbeers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDescription;

        public ViewHolder(View v) {
            super(v);

            textViewTitle = (TextView) v.findViewById(R.id.text_view_title);
            textViewDescription = (TextView) v.findViewById(R.id.text_view_description);
        }
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
        viewHolder.textViewDescription.setText(beer.getDescription());
    }

    @Override
    public int getItemCount() {
        return mBeers.size();
    }

    public void updateBeers(List<Beer> beers) {
        mBeers = beers;
        notifyDataSetChanged();
    }
}