package renatoprobst.offlinewearforspotify.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.OnClick;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.Track;
import renatoprobst.mobileandwear.model.TrackList;
import renatoprobst.mobileandwear.service.MusicPlayerInterface;
import renatoprobst.mobileandwear.service.MusicPlayerService;
import renatoprobst.mobileandwear.service.SpotifyPlayerController;
import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.activity.generic.GenericSingleActivity;

public class PlayerActivity extends GenericSingleActivity<Track> implements MusicPlayerInterface {


    @Bind(R.id.play)
    ImageView play;
    @Bind(R.id.next)
    ImageView next;
    @Bind(R.id.previous)
    ImageView previous;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.time)
    TextView time;

    private Object currentPlayingObject; // playlist, album, track
    private Gson gson;

    public PlayerActivity() {
        layout = R.layout.activity_player;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SpotifyPlayerController.ACTION_STATE_PLAY)) {
                play.setImageResource(android.R.drawable.ic_media_pause);
            } else if (action.equals(SpotifyPlayerController.ACTION_STATE_PAUSE)) {
                play.setImageResource(android.R.drawable.ic_media_play);
            } else if (action.equals(SpotifyPlayerController.ACTION_STATE_TRACKINFO)) {
                object = new Gson().fromJson(intent.getExtras().getString("object"), Track.class);
                title.setText(object.name);
                time.setText(object.artists.get(0).name + " - " + object.album.name);
               // play.setImageResource(android.R.drawable.ic_media_pause);

            }
        }
    };

    private void broadcastAction(String action) {
        Intent intent = new Intent(action);
        intent.setComponent(new ComponentName(this, MusicPlayerService.class));
        startService(intent);
    }

    private void broadcastAction(String action, Object object) {

        String data = object.getClass() == String.class ? (String) object : new Gson().toJson(object);
        Intent intent = new Intent(action);
        intent.setComponent(new ComponentName(this, MusicPlayerService.class));
        intent.putExtra("object", data);
        intent.putExtra("type", object.getClass().getSimpleName());
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void setData() {
        Log.w(TAG, currentPlayingObject != null ? currentPlayingObject.getClass().getSimpleName() : "current object is null");
        play(currentPlayingObject);
    }

    @Override
    public void play(Object object) {
        broadcastAction(MusicPlayerService.ACTION_PLAYURI, object);
    }

    @OnClick(R.id.play)
    @Override
    public void playPause() {
        broadcastAction(MusicPlayerService.ACTION_TOGGLE_PLAYBACK);
    }

    @Override
    public void stop() {

    }

    @Override
    @OnClick(R.id.next)
    public void next() {
        broadcastAction(MusicPlayerService.ACTION_NEXT);
    }

    @Override
    @OnClick(R.id.previous)
    public void previous() {

        broadcastAction(MusicPlayerService.ACTION_PREV);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        gson = new Gson();
    }

    @Override
    public void getData() {



        if (getIntent().getExtras() == null) {
            return;
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(SpotifyPlayerController.ACTION_STATE_PLAY);
        filter.addAction(SpotifyPlayerController.ACTION_STATE_PAUSE);
      //  filter.addAction(SpotifyPlayerController.ACTION_STATE_NEXT);
       // filter.addAction(SpotifyPlayerController.ACTION_STATE_PREVIOUS);
        filter.addAction(SpotifyPlayerController.ACTION_STATE_TRACKINFO);

        registerReceiver(receiver, filter);

        Bundle extras = getIntent().getExtras();

        if (extras.containsKey("type") && extras.containsKey("object")) {
            String objectType = extras.getString("type");
            Log.w(TAG, "object type: " + objectType);

            switch (objectType) {
                case "PlaylistSimple":
                    currentPlayingObject = gson.fromJson(extras.getString("object"), PlaylistSimple.class);
                    break;
                case "Track":
                    currentPlayingObject = gson.fromJson(extras.getString("object"), Track.class);
                    break;
                case "TrackList":
                    currentPlayingObject = gson.fromJson(extras.getString("object"), TrackList.class);
                    break;
                case "SavedAlbum":
                    currentPlayingObject = gson.fromJson(extras.getString("object"), SavedAlbum.class);
                    break;
                case "String":
                    currentPlayingObject = extras.getString("object");
                    break;
                default:
                    return;
            }

        }

        setData();


    }

}