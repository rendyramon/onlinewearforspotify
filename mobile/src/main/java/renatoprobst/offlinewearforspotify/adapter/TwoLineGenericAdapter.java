package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.adapter.generic.GenericListAdapter;

/**
 * Created by Renato on 27/04/2016.
 */
public abstract class TwoLineGenericAdapter<G> extends GenericListAdapter<G, TwoLineGenericAdapter.ItemViewHolder> {

    public TwoLineGenericAdapter(Context context, List<G> list) {
        super(context, list);
    }

    @Override
    protected ItemViewHolder getAdapterHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    // Provide a reference to the type of views you're using
    public class ItemViewHolder extends GenericListAdapter.ItemViewHolder {
        @Bind(R.id.title)
        TextView titleView;
        @Bind(R.id.subTitle)
        TextView subTitleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(TwoLineGenericAdapter.ItemViewHolder holder, final int position) {

        G item = list.get(position);
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        TextView titleView = itemHolder.titleView;
        TextView subTitleView = itemHolder.subTitleView;

        titleView.setText(getTitle(item));
        subTitleView.setText(getSubTitle(item));

        if (getOnItemClickListener() != null) {
            holder.setOnClickListener(position, item, getOnItemClickListener());
        }

        holder.itemView.setTag(position);
    }

    abstract protected String getTitle(G item);
    abstract protected String getSubTitle(G item);
}