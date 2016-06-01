package renatoprobst.offlinewearforspotify.activity.generic;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.adapter.generic.GenericListAdapter;

/**
 * Created by Renato on 29/04/2016.
 */
public abstract class GenericListActivity<G> extends GenericActivity<G> {
    protected List<G> list = new ArrayList<G>();
    @Nullable
    @Bind(R.id.listView)
    protected RecyclerView listView;
    @Nullable
    @Bind(R.id.pager)
    protected ViewPager pager;
    protected GenericListAdapter listAdapter;


    public GenericListActivity() {
        layout = R.layout.activity_playlist_list;
    }


    @Override
    protected void onCreated() {
        super.onCreated();
        listView.setLayoutManager(new LinearLayoutManager(this));
    }
}
