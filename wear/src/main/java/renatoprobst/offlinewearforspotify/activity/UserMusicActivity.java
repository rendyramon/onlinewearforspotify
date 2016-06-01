package renatoprobst.offlinewearforspotify.activity;

import android.content.Intent;
import android.support.wearable.view.WearableListView;

import renatoprobst.mobileandwear.dao.SpotifyDao;
import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.activity.generic.GenericListActivity;
import renatoprobst.offlinewearforspotify.adapter.MenuListAdapter;
import renatoprobst.mobileandwear.model.MenuModel;

public class UserMusicActivity extends GenericListActivity<MenuModel> {

    private SpotifyDao spotifyDao;

    public UserMusicActivity() {
        layout = R.layout.activity_playlist_list;
    }

    @Override
    public void getData() {
        setData();
    }

    @Override
    public void setData() {
        list.clear();
        list.add(new MenuModel(1, "Playlists", "olaylists"));
        list.add(new MenuModel(2, "Musics", "tracks"));
        list.add(new MenuModel(3, "Albums", "albums"));
        list.add(new MenuModel(4, "Artists", "artists"));


        listView.setAdapter(new MenuListAdapter(this, list));

        listView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                int position = (int) viewHolder.itemView.getTag();
                MenuModel menu = list.get(position);

                Class activity = null;

              switch (menu.getTag()) {
                  case "playlists":
                      activity = PlaylistListUserActivity.class;
                      break;
                  case "albums":
                      activity = AlbumListUserActivity.class;
                      break;
                  case "tracks":
                      activity = TrackListUserActivity.class;
                      break;
                  case "artists":
                      activity = ArtistListUserActivity.class;
                      break;
                  default:
                      return;
              }

                startActivity(new Intent(UserMusicActivity.this, activity));

            }

            @Override
            public void onTopEmptyRegionClick() {

            }
        });
    }

}
