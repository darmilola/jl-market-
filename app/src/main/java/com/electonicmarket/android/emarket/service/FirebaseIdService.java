package com.electonicmarket.android.emarket.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh(){
        String token = FirebaseInstanceId.getInstance().getToken();
        saveFirebaseidInSharedPrefences(token);




    }



    private void saveFirebaseidInSharedPrefences(String fid){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firebaseid",fid);
        editor.commit();

    }

}
