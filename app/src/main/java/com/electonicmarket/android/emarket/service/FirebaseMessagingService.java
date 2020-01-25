package com.electonicmarket.android.emarket.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        //Log.e("from",remoteMessage.getFrom());
        //Log.e("body", remoteMessage.getNotification().getBody() );

        if(remoteMessage.getData().size() > 0){

            Log.e("data", remoteMessage.getData().toString());

            try{
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                handledatamessage(jsonObject);

            }catch (Exception e){
                Log.e("error", e.getMessage() );
            }

        }
    }

    private void handledatamessage(JSONObject JSON) {

        try {
            JSONObject data = JSON.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

            String channelid = "JL Market";
            Intent intent = new Intent(this,splashscreen.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.bigText(message);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelid)
                    .setSmallIcon(R.drawable.appicon)
                    .setStyle(bigTextStyle)
                    .setChannelId(channelid)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = "JL Market";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(channelid,name,importance);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(1, builder.build());
        } catch (JSONException E) {
            Log.e("error in json", E.getMessage());
        }
    }

}
