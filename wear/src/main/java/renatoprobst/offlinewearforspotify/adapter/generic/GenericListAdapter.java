package renatoprobst.offlinewearforspotify.adapter.generic;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import renatoprobst.offlinewearforspotify.R;

/**
 * Created by Renato on 27/04/2016.
 */
public abstract class GenericListAdapter<E, G extends GenericListAdapter.ItemViewHolder> extends WearableListView.Adapter {
    protected List<E> list;
    protected final Context context;
    protected final LayoutInflater inflater;
    protected int layout = R.layout.adapter_playlist;

    // Provide a suitable constructor (depends on the kind of dataset)
    public GenericListAdapter(Context context, List<E> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    protected abstract G getAdapterHolder(ViewGroup parent);

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                layout, parent, false);
        G viewHolder = getAdapterHolder((ViewGroup) layoutView);

        return viewHolder;
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    // Provide a reference to the type of views you're using
    public abstract class ItemViewHolder extends WearableListView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}