package renatoprobst.mobileandwear.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Renato on 01/05/2016.
 */
public class MenuModel extends MyModel {
    private String title;
    private String tag;
    private Drawable iconDrawable;
    private int icon;

    public MenuModel(int id, String title, String tag) {

        this.id = String.valueOf(id);
        this.title = title;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
