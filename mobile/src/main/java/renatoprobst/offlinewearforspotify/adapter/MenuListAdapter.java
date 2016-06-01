package renatoprobst.offlinewearforspotify.adapter;

import android.content.Context;

import java.util.List;

import renatoprobst.mobileandwear.model.MenuModel;

/**
 * Created by Renato on 27/04/2016.
 */
public class MenuListAdapter extends TwoLineGenericAdapter<MenuModel> {

    public MenuListAdapter(Context context, List<MenuModel> list) {
        super(context, list);
    }

    @Override
    protected String getTitle(MenuModel item) {
        return item.getTitle();
    }

    @Override
    protected String getSubTitle(MenuModel item) {
        return "";
    }
}