package renatoprobst.offlinewearforspotify.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import renatoprobst.offlinewearforspotify.R;
import renatoprobst.offlinewearforspotify.activity.UserMusicActivity;
import renatoprobst.offlinewearforspotify.adapter.generic.GenericGridAdapter;

/**
 * Created by Renato on 27/04/2016.
 */
public class MenuGridAdapter extends GenericGridAdapter {

    public MenuGridAdapter(Activity activity) {
        super(activity);
    }

    @Override
    protected void setPages() {
        pages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();
        row1.addPages(new SimplePage("My music", "See my music", "mymusic"));
        row1.addPages(new SimplePage("Search", "search", "search"));

        pages.add(row1);
    }

    @Override
    protected void setViewBehavior(View view, final SimplePage page) {
        final TextView textView = (TextView) view.findViewById(R.id.name);
        textView.setText(page.mTitle);

        view.setTag(page.mTitle);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (page.tag) {
                    case "search":
                        break;
                    case "mmymusic":
                        activity.startActivity(new Intent(activity, UserMusicActivity.class));
                        break;
                    default:
                        break;
                }

            }
        });
    }
}
