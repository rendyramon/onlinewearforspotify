package renatoprobst.offlinewearforspotify.activity.generic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import renatoprobst.offlinewearforspotify.R;

/**
 * Created by Renato on 29/04/2016.
 */
public abstract class GenericActivity<G> extends AppCompatActivity implements GenericActivityInterface{

    protected final String TAG = this.getClass().getSimpleName();
    protected int layout;
    @Bind(R.id.container)
    protected ViewGroup mContainerView;

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

}
