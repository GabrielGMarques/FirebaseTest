package com.gabrielgomarques.firebasetest.ui.activity;

/**
 * Created by Gabriel on 25/03/2017.
 */

public interface NavigationListener {

    /**
     * Method called when a item is selected on drawer layout
     * @param position of DrawerItem
     * @return previous selected item
     */
    public int onSelectItem(int position);
}
