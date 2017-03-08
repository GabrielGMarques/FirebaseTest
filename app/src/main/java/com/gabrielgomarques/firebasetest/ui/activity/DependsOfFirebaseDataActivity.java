package com.gabrielgomarques.firebasetest.ui.activity;

/**
 * Created by Gabriel on 12/01/2017.
 */

public interface DependsOfFirebaseDataActivity <T>{
    public void onStartRequest();
    public  void onFinishRequest();
    public void onSuccessRequest(T obj);
    public void onFailRequest();
}
