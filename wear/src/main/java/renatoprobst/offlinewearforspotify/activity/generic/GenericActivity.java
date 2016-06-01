package renatoprobst.offlinewearforspotify.activity.generic;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import renatoprobst.offlinewearforspotify.R;

/**
 * Created by Renato on 29/04/2016.
 */
public abstract class GenericActivity<G> extends WearableActivity implements GenericActivityInterface{

    protected final String TAG = this.getClass().getSimpleName();
    protected int layout;
    @Bind(R.id.container)
    protected BoxInsetLayout mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        ButterKnife.bind(this);
        onCreated();
        getData();
    }

    protected void onCreated() {

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));

        } else {
            mContainerView.setBackground(null);

        }
    }




}
