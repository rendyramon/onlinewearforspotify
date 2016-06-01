package renatoprobst.offlinewearforspotify.activity;

import android.util.Log;

import renatoprobst.mobileandwear.service.ConnectionHelper;
import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.activity.generic.GenericListActivity;
import renatoprobst.offlinewearforspotify.adapter.MenuGridAdapter;

public class MenuActivity extends GenericListActivity<MenuGridAdapter.SimpleRow> {

    private String spotifyToken;

    public MenuActivity() {
        layout = R.layout.activity_menu;
    }

    @Override
    public void getData() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("spotifyToken")) {
            spotifyToken = getIntent().getExtras().getString("spotifyToken");
            Log.w(TAG, "spotify token: " + spotifyToken);

            ConnectionHelper.saveToken(this, spotifyToken);
            setData();
        }
    }

    @Override
    public void setData() {
        pager.setAdapter(new MenuGridAdapter(this));
    }
}
