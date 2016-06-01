package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.adapter.generic.GenericListAdapter;
import renatoprobst.mobileandwear.model.MenuModel;

/**
 * Created by Renato on 27/04/2016.
 */
public class MenuListAdapter extends GenericListAdapter<MenuModel, MenuListAdapter.ItemViewHolder> {

    public MenuListAdapter(Context context, List<MenuModel> list) {
        super(context, list);
    }

    @Override
    protected ItemViewHolder getAdapterHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {

        MenuModel item = list.get(position);
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        TextView view = itemHolder.titleView;
        view.setText(item.getTitle());

        holder.itemView.setTag(position);
    }

    // Provide a reference to the type of views you're using
    public class ItemViewHolder extends GenericListAdapter.ItemViewHolder {
        @Bind(R.id.title)
        TextView titleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}