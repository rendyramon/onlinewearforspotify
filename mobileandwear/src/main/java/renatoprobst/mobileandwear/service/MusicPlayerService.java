package renatoprobst.mobileandwear.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedAlbum;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import renatoprobst.mobileandwear.dao.SpotifyDao;
import renatoprobst.mobileandwear.model.TrackList;
import renatoprobst.mobileandwear.util.MusicPlayer;

/**
 * Created by Renato on 29/04/2016.
 */
public class MusicPlayerService extends Service implements MusicPlayerInterface {

    private static final String TAG = "MusicPlayerService";
    private Player spotifyPlayer;
    private SpotifyPlayerController spotifyPlayerController;
    private MusicPlayer musicPlayer = new MusicPlayer();
    private String spotifyToken;
    private SpotifyDao spotifyDao;
    private ConnectionHelper connectionHelper;
    private SpotifyPlayerController.OnReadyCallback onReadyCallback;

    //private MediaSessionManager mManager;
    private MediaSessionCompat mSession;
    private MediaControllerCompat mController;

    public static final String ACTION_TOGGLE_PLAYBACK = "renatoprobst.mobileandwear.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAYURI = "renatoprobst.mobileandwear.PLAYURI";
    public static final String ACTION_PREV = "renatoprobst.mobileandwear.PREV";
    public static final String ACTION_NEXT = "renatoprobst.mobileandwear.NEXT";
    private Gson gson = new Gson();

    NotificationController notificationController;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification(intent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        connectionHelper = new ConnectionHelper();
        spotifyDao = new SpotifyDao(getApplicationContext());
        spotifyToken = ConnectionHelper.getToken(getApplicationContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction(SpotifyPlayerController.ACTION_STATE_PLAY);
        filter.addAction(SpotifyPlayerController.ACTION_STATE_PAUSE);
        filter.addAction(SpotifyPlayerController.ACTION_STATE_NEXT);
        filter.addAction(SpotifyPlayerController.ACTION_STATE_PREVIOUS);
        filter.addAction(SpotifyPlayerController.ACTION_STATE_TRACKINFO);

        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.w(TAG, "onStartCommand");

        if (spotifyPlayer == null) {
            setPlayer();
        }
        if (mController == null) {
            startMediaService();
        }

        handleIntent(intent);

        return START_STICKY;
    }

    private void handleIntent(Intent intent) {

        if (intent == null || intent.getAction() == null)
            return;

        Log.w(TAG, "handling intent");

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_TOGGLE_PLAYBACK)) {
            mController.getTransportControls().play();
        } else if (action.equalsIgnoreCase(ACTION_PREV)) {
            mController.getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(ACTION_PLAYURI)) {
            Object object = parseObjectToPlay(intent.getExtras());
            play(object);
        }
    }

    private Object parseObjectToPlay(Bundle bundle) {

        String objectType = bundle.getString("type");
        Log.w(TAG, "object type: " + objectType);
        Object object = null;

        switch (objectType) {
            case "PlaylistSimple":
                object = gson.fromJson(bundle.getString("object"), PlaylistSimple.class);
                break;
            case "String":
                object = bundle.getString("object");
                break;
            case "SavedAlbum":
                object = gson.fromJson(bundle.getString("object"), SavedAlbum.class);
                break;
            case "Track":
                object = gson.fromJson(bundle.getString("object"), Track.class);
                break;
            case "TrackList":
                object = gson.fromJson(bundle.getString("object"), TrackList.class);
                break;
            default:
                break;
        }

        return object;
    }

    private void updateNotification(Intent intent) {
        String action = intent.getAction();
        int icon = android.R.drawable.ic_media_pause;
        boolean update = false;
        if (action.equals(SpotifyPlayerController.ACTION_STATE_PLAY)) {
            icon = android.R.drawable.ic_media_pause;
            update = true;
        } else if (action.equals(SpotifyPlayerController.ACTION_STATE_PAUSE)) {
            icon = android.R.drawable.ic_media_play;
            update = true;
        } else if (action.equals(SpotifyPlayerController.ACTION_STATE_TRACKINFO)) {
            Track track = (Track) parseObjectToPlay(intent.getExtras());
            notificationController.title = track.name;
            notificationController.subTitle = track.artists.get(0).name + " - " + track.album.name;
            update = true;
        }
        notificationController.icon = icon;

        if (update) {
            android.support.v4.app.NotificationCompat.Action actionButton =
                    new android.support.v4.app.NotificationCompat.Action(icon, "pause", notificationController.retreivePlaybackAction(1));
            notificationController.buildNotification(actionButton);
        }
    }


    private void startMediaService() {
        // mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                                            @Override
                                            public void onAudioFocusChange(int focusChange) {

                                            }
                                        }, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        final ComponentName componentName = new ComponentName(this, MusicPlayerService.class);
        mSession = new MediaSessionCompat(getApplicationContext(), "spotifyOfflineMediaPlayer", componentName, null);

        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                Log.w(TAG, "onMediaButtonEvent");
                return super.onMediaButtonEvent(mediaButtonEvent);
            }

            @Override
            public void onPlay() {
                super.onPlay();
                playPause();

            }

            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                Log.w(TAG, "onSkipToNext");
                spotifyPlayerController.next();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                spotifyPlayerController.previous();
            }

            @Override
            public void onStop() {
                super.onStop();
            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY)
                .setState(PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0)
                .build();

        // mSession.setPlaybackState(state);

        mSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build());

        mSession.setActive(true);


        mController = mSession.getController();
        final MediaControllerCompat.TransportControls controls = mSession.getController().getTransportControls();

        notificationController = new NotificationController(this, mSession);

    }

    public void playUri(String uri, Object item) {
        if (spotifyPlayer == null || !spotifyPlayer.isInitialized() || !spotifyPlayer.isLoggedIn()) {
            setPlayer();
        } else {
            Log.w(TAG, "playing uri");
            if (item != null) {
                if (item.getClass() == PlaylistSimple.class) {

                } else if (item.getClass() == TrackSimple.class) {
                    musicPlayer.setCurrentTrack((TrackSimple) item);
                }
            }
            spotifyPlayer.play(uri);
        }
    }

    public void setPlayer() {
        Log.w(TAG, "settting player");

        Config playerConfig = new Config(getApplicationContext(), spotifyToken, ConnectionHelper.CLIENT_ID);

        spotifyPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {
                Log.w(TAG, "player initialized, adding listeners");
                spotifyPlayerController = new SpotifyPlayerController(getApplicationContext(), spotifyPlayer);
                spotifyPlayer.addConnectionStateCallback(connectionHelper);

                if (onReadyCallback != null) {
                    onReadyCallback.onReady();
                }

            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        Spotify.destroyPlayer(this);
        mSession.release();
        unregisterReceiver(receiver);
    }


    @Override
    public void play(final Object object) {
        if (spotifyPlayer == null || !spotifyPlayer.isInitialized() || !spotifyPlayer.isLoggedIn()) {
            Log.w(TAG, "song play will happen on onreadyCallback");
            onReadyCallback = new SpotifyPlayerController.OnReadyCallback() {
                @Override
                public void onReady() {
                    Log.w(TAG, "onreadyCallback");
                    spotifyPlayerController.play(object);
                }
            };
        } else {
            spotifyPlayerController.play(object);
        }
    }

    @Override
    public void playPause() {
        spotifyPlayerController.playPause();
    }

    @Override
    public void stop() {
        spotifyPlayerController.stop();
    }

    @Override
    public void next() {
        spotifyPlayerController.next();
    }

    @Override
    public void previous() {
        spotifyPlayerController.previous();
    }
}
