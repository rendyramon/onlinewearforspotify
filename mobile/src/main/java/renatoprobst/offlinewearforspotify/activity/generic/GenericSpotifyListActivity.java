package renatoprobst.offlinewearforspotify.activity.generic;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.gson.Gson;

import renatoprobst.mobileandwear.dao.SpotifyDao;
import renatoprobst.offlinewearforspotify.activity.PlayerActivity;
import renatoprobst.offlinewearforspotify.adapter.generic.GenericListAdapter;
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

        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void getData() {


    }

    @Override
    public void setData() {
        Log.w(TAG, "success getting data");
        Log.w(TAG, "playlist size: " + list.size());
        listView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new GenericListAdapter.OnItemClickListener<G>() {
            @Override
            public void onItemClickListener(int position, G object) {
                Intent intent = new Intent(GenericSpotifyListActivity.this, PlayerActivity.class);
                intent.putExtra("object", new Gson().toJson(list.get(position)));
                Log.w(TAG, "class: " + list.get(position).getClass().getSimpleName());
                intent.putExtra("type", list.get(position).getClass().getSimpleName());
                startActivity(intent);
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
