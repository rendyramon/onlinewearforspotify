package renatoprobst.offlinewearforspotify.activity.generic;

import android.content.Intent;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.gson.Gson;

import renatoprobst.mobileandwear.dao.SpotifyDao;
import renatoprobst.offlinewearforspotify.activity.PlayerActivity;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by Renato on 01/05/2016.
 */
public class GenericSpotifyListActivity<G> extends GenericListActivity<G> {

    protected SpotifyDao spotifyDao;

    @Override
    protected void onCreated() {
        super.onCreated();
        spotifyDao = new SpotifyDao(this);
    }

    @Override
    public void getData() {


    }

    @Override
    public void setData() {
        Log.w(TAG, "success getting data");
        Log.w(TAG, "playlist size: " + list.size());
        listView.setAdapter(listAdapter);
        listView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                int position = (int) viewHolder.itemView.getTag();
                Intent intent = new Intent(GenericSpotifyListActivity.this, PlayerActivity.class);
                intent.putExtra(list.get(position).getClass().getSimpleName(), new Gson().toJson(list.get(position)));
                intent.putExtra("type", list.get(position).getClass().getSimpleName());
                startActivity(intent);
            }

            @Override
            public void onTopEmptyRegionClick() {
                Log.w(TAG, "onTopEmptyRegionClick");
            }
        });
    }

    protected void logRetrofitError(RetrofitError error) {
        Log.w(TAG, "retrofit failure");
        Log.w(TAG, error.getMessage());
        try {
            String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
            Log.v("failure", json.toString());
        } catch (Exception e) {
        }
    }
}
