package com.gabrielgomarques.firebasetest.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Gabriel on 26/03/2017.
 */

public class MessageUtil {

    private MessageUtil() {
    }

    private static MessageUtil instance;

    public static MessageUtil getInstance(){
        if(instance==null)
            instance= new MessageUtil();
        return  instance;
    }

    public void showSnacbar(View view , String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).setAction("Action",null).show();
    }
}
