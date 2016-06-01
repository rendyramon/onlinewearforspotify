package renatoprobst.offlinewearforspotify.activity;

import android.content.Intent;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;
import renatoprobst.mobileandwear.model.TrackList;
import renatoprobst.offlinewearforspotify.activity.generic.GenericSpotifyListActivity;
import renatoprobst.offlinewearforspotify.adapter.TrackAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrackListUserActivity extends GenericSpotifyListActivity<Track> {

    @Override
    public void getData() {
        spotifyDao.getService().getMySavedTracks(spotifyDao.getLoadingCallback(new Callback<Pager<SavedTrack>>() {
            @Override
            public void success(Pager<SavedTrack> playlistSimplePager, Response response) {
                parseList(playlistSimplePager.items);
                listAdapter = new TrackAdapter(TrackListUserActivity.this, list);
                setData();
            }

            @Override
            public void failure(RetrofitError error) {
                logRetrofitError(error);
            }
        }));
    }

    private void parseList(List<SavedTrack> items) {
        if (items != null) {
            list.clear();
            for (SavedTrack savedTrack : items) {
                list.add(savedTrack.track);
            }
        }
    }


    @Override
    public void setData() {
        super.setData();

        listView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {


                final int position = (int) viewHolder.itemView.getTag();
                final Track currentTrack = list.get(position);

                List<Track> filteredList = new ArrayList<Track>();
                if (position > 0) {
                    for (int x = position; x < list.size(); x++) {
                        filteredList.add(list.get(x));
                    }
                }

                final TrackList trackList = new TrackList(TrackList.Type.Track, filteredList, currentTrack);

                Intent intent = new Intent(TrackListUserActivity.this, PlayerActivity.class);
                intent.putExtra("object", new Gson().toJson(trackList));
                intent.putExtra("type", "TrackList");
                startActivity(intent);
                Log.w(TAG, "passing tracklist");
            }

            @Override
            public void onTopEmptyRegionClick() {
                Log.w(TAG, "onTopEmptyRegionClick");
            }
        });
    }
}
