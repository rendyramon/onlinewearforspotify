package renatoprobst.mobileandwear.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import renatoprobst.mobileandwear.dao.SpotifyDao;
import renatoprobst.mobileandwear.model.TrackList;
import renatoprobst.mobileandwear.util.MusicPlayer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renato on 30/04/2016.
 */
public class SpotifyPlayerController implements
        PlayerNotificationCallback, MusicPlayerInterface {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private Player spotifyPlayer;
    public MusicPlayer.State playerState;
    public Gson gson;

    public static final String ACTION_STATE_TRACKINFO = "renatoprobst.mobileandwear.state.trackinfo";
    public static final String ACTION_STATE_PLAY = "renatoprobst.mobileandwear.state.play";
    public static final String ACTION_STATE_PAUSE = "renatoprobst.mobileandwear.state.pause";
    public static final String ACTION_STATE_NEXT = "renatoprobst.mobileandwear.state.next";
    public static final String ACTION_STATE_PREVIOUS = "renatoprobst.mobileandwear.state.previous";
    private SpotifyDao spotifyDao;
    public Track currentTrack;


    public SpotifyPlayerController(Context context, Player spotifyPlayer) {
        this.context = context;

        this.spotifyPlayer = spotifyPlayer;
        this.spotifyPlayer.addPlayerNotificationCallback(this);
        spotifyDao = new SpotifyDao(this.context);
        gson = new Gson();
    }

    // object come as json
    @Override
    public void play(Object object) {
        String uri = null;
        if (object.getClass() == String.class) {
            uri = (String) object;
        } else if (object.getClass() == PlaylistSimple.class) {
            uri = ((PlaylistSimple) object).uri;
        }
        else if (object.getClass() == SavedAlbum.class) {
            Album album = ((SavedAlbum) object).album;
            solveAlbum(album);
            return;

        }
        else if (object.getClass() == TrackList.class) {
            TrackList trackList = ((TrackList) object);
            solveTrackList(trackList);
            return;

        }
        else if (object.getClass() == Track.class) {
            uri = ((Track) object).uri;
        }

        if (uri != null) {
            Log.w(TAG, "trying to play uri: " + uri);
            spotifyPlayer.play(uri);
        }
    }

    private void solveTrackList(TrackList trackList) {
        List<String> trackUris = new ArrayList<>();
        trackUris.add(trackList.getCurrentTrack().uri);
        for (Track track : trackList.getTracks()) {
            if (track.id != trackList.getCurrentTrack().id) {
                trackUris.add(track.uri);
            }
        }
        spotifyPlayer.play(trackUris);
    }

    private void solveAlbum(Album album) {
        List<String> trackUris = new ArrayList<>();
        for (TrackSimple track : album.tracks.items) {
            trackUris.add(track.uri);
        }
        spotifyPlayer.play(trackUris);
    }

    @Override
    public void playPause() {
        spotifyPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                if (playerState.playing) {
                    spotifyPlayer.pause();
                } else {
                    spotifyPlayer.resume();
                }
            }
        });
    }

    @Override
    public void next() {
        spotifyPlayer.skipToNext();
    }

    @Override
    public void previous() {
        spotifyPlayer.skipToPrevious();
    }

    @Override
    public void stop() {
        spotifyPlayer.logout();
    }


    @Override
    public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {
        Log.d(TAG, "Playback vent received: " + eventType.name());
        switch (eventType) {
            case TRACK_CHANGED:
                this.playerState = MusicPlayer.State.PLAYING;
                getCurrentTrackInfo(playerState.trackUri);
                break;
            case PLAY:
                this.playerState = MusicPlayer.State.PLAYING;
                break;
            case PAUSE:
                this.playerState = MusicPlayer.State.PAUSED;
                break;
            case SKIP_NEXT:
                this.playerState = MusicPlayer.State.PLAYING;
                break;
            case SKIP_PREV:
                this.playerState = MusicPlayer.State.PLAYING;
                break;
            default:
                break;
        }
        Log.w(TAG, "eventType " + eventType.name());

        syncState(eventType);
    }

    private void syncState(EventType eventType) {
        String intentFilterAction = null;
        switch (eventType) {
            case PLAY:
                intentFilterAction = SpotifyPlayerController.ACTION_STATE_PLAY;
                break;
            case PAUSE:
                intentFilterAction = SpotifyPlayerController.ACTION_STATE_PAUSE;
                break;
            case SKIP_NEXT:
                intentFilterAction = SpotifyPlayerController.ACTION_STATE_NEXT;
                break;
            case SKIP_PREV:
                intentFilterAction = SpotifyPlayerController.ACTION_STATE_PREVIOUS;
                break;
            default:
                break;
        }
        if (intentFilterAction != null) {
            context.sendBroadcast(new Intent(intentFilterAction));
        }
    }


    private void getCurrentTrackInfo(String uri) {

        Log.w(TAG, "getting track info");

        String id = uri.replace("spotify:track:", "");

        spotifyDao.getService().getTrack(id, new Callback<Track>() {
            @Override
            public void success(Track track, Response response) {

                SpotifyPlayerController.this.currentTrack = track;
                Intent intent = new Intent(ACTION_STATE_TRACKINFO);
                intent.putExtra("object", gson.toJson(track));
                intent.putExtra("type", "Track");
                context.sendBroadcast(intent);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onPlaybackError(PlayerNotificationCallback.ErrorType errorType, String errorDetails) {
        Log.d(TAG, "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }


    public interface OnReadyCallback {
        void onReady();
    }
}
