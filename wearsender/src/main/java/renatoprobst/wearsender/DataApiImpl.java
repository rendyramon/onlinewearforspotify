package renatoprobst.wearsender;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.wearable.DataApi;

import renatoprobst.wearsender.listener.SendResultListener;
import renatoprobst.wearsender.util.Payload;

public interface DataApiImpl {

    /**
     * sync data without listener
     *
     * @param payload
     */
    public void syncData(@NonNull final Payload payload);

    /**
     * sync data
     *
     * @param payload
     * @param listener
     */
    public void syncData(@NonNull final Payload payload, @Nullable final SendResultListener<DataApi.DataItemResult> listener);

    /**
     * sync data using AsyncTask without listener
     * if you want to call from UI thread
     *
     * @param payload
     */
    public void syncDataAsync(@NonNull final Payload payload);

    /**
     * sync data using AsyncTask
     * if you want to call from UI thread
     *
     * @param payload
     * @param listener
     */
    public void syncDataAsync(@NonNull final Payload payload, @Nullable final SendResultListener<DataApi.DataItemResult> listener);
}
