package renatoprobst.offlinewearforspotify.activity.generic;

import android.support.annotation.Nullable;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WearableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import renatoprobst.offlinewearforspotify.R;

/**
 * Created by Renato on 29/04/2016.
 */
public abstract class GenericListActivity<G> extends GenericActivity<G> {
    protected List<G> list = new ArrayList<G>();
    @Nullable
    @Bind(R.id.listView)
    protected WearableListView listView;
    @Nullable
    @Bind(R.id.pager)
    protected GridViewPager pager;
    protected WearableListView.Adapter listAdapter;


    public GenericListActivity() {
        layout = R.layout.activity_playlist_list;
    }
}
