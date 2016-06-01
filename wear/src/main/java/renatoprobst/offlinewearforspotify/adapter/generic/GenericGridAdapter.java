package renatoprobst.offlinewearforspotify.adapter.generic;

import android.app.Activity;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import renatoprobst.offlinewearforspotify.R;

/**
 * Created by Renato on 27/04/2016.
 */
public abstract class GenericGridAdapter<E> extends GridPagerAdapter {

    protected Activity activity;
    protected ArrayList<SimpleRow> pages;
    protected LayoutInflater inflater;
    protected List<E> list = new ArrayList<>();

    public GenericGridAdapter(Activity activity, List<E> list) {
        this.activity = activity;
        this.list = list;
        this.inflater = LayoutInflater.from(this.activity);
        setPages();
    }

    public GenericGridAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(this.activity);
        setPages();
    }

    protected abstract void setPages();


    @Override
    public int getRowCount() {
        return pages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return pages.get(row).size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int row, int col) {
        final SimplePage page = ((SimpleRow) pages.get(row)).getPages(col);
        final View view = inflater.inflate(R.layout.adapter_menu, container, false);

        setViewBehavior(view, page);
        container.addView(view);

        return view;
    }

    protected abstract void setViewBehavior(View view, SimplePage page);

    @Override
    public void destroyItem(ViewGroup container, int row, int col, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public class SimplePage {

        public String mTitle;
        public String mText;
        public String tag;
        public int mIconId;
        public int mBackgroundId;

        public SimplePage(String title, String text, int iconId, int backgroundId) {
            this.mTitle = title;
            this.mText = text;
            this.mIconId = iconId;
            this.mBackgroundId = backgroundId;
        }

        public SimplePage(String title, String text, String tag) {
            this.mTitle = title;
            this.mText = text;
            this.tag = tag;
        }
    }

    public class SimpleRow {

        ArrayList<SimplePage> mPagesRow = new ArrayList<SimplePage>();

        public void addPages(SimplePage page) {
            mPagesRow.add(page);
        }

        public SimplePage getPages(int index) {
            return mPagesRow.get(index);
        }

        public int size() {
            return mPagesRow.size();
        }
    }
}
