package com.chatapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.chatapp.R;
import com.chatapp.activities.ChatActivity;
import com.chatapp.pojo.PojoService;
import com.chatapp.utils.SharedPreferenceWriter;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;
import me.leolin.shortcutbadger.ShortcutBadger;

public class ServiceChat extends Service {
    int count = 1;
    Firebase reference1;
    public Vibrator vibrator;
    private ArrayList<PojoService> al_chat_list = new ArrayList<>();
    private ArrayList<String> al_userId = new ArrayList<>();
    private  String messageRepeate="";


    public ServiceChat() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(">>>", "startService");
        Firebase.setAndroidContext(getApplicationContext());

        String chatWith = SharedPreferenceWriter.getInstance(getApplicationContext()).getString("chatWith");
        final String username = SharedPreferenceWriter.getInstance(getApplicationContext()).getString("username");
        reference1 = new Firebase("https://chatapp-9dacf.firebaseio.com/user");
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.child("Detail").child("name").getValue().toString();

if (!username.equals(name)){



                String message = dataSnapshot.child("Detail").child("lastMsg").getValue().toString();
                String userName = dataSnapshot.child("Detail").child("senderName").getValue().toString();
                String isRead = dataSnapshot.child("Detail").child("isRead").getValue().toString();
                al_userId.add(userName);
                Set<String> hashsetList = new HashSet<String>(al_userId);
                if (username.equals(name)) {
                } else {
                    PojoService pojoChat = new PojoService();
                    pojoChat.setMessage(message);
                    pojoChat.setSenderName(userName);
                    if (!messageRepeate.equals(message)){
                        al_chat_list.add(pojoChat);

                        if (isRead.equals("true")){
                            addNotification(message, userName, hashsetList.size());
                        }
                    }
                    messageRepeate=message;
                }
}
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    /*    reference1.orderByKey().limitToLast(1);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    //  String name = (String) messageSnapshot.child("name").getValue();
                    String message = (String) messageSnapshot.child("message").getValue();
                    String userName = (String) messageSnapshot.child("user").getValue();


                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });*/
       /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("messages/" +username + "_" +chatWith).orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
          *//*  @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.child("message").getValue().toString();
            }*//*

           // Unterminated object at character 52 of {-M-P3IDkyC-RnUS-HFui={message=jf, time=2020-02-06 06:34, forward_mes=, forward_count=0, s_image=, userId=65, image=, user=sunil}}@Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
            //    String message = dataSnapshot.child("message").getValue().toString();
                for (com.google.firebase.database.DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    //  String name = (String) messageSnapshot.child("name").getValue();
                    String message = (String) messageSnapshot.child("message").getValue();
                    String userName = (String) messageSnapshot.child("user").getValue();

                    if (userName.equals(Constants.username)) {

                    } else {
                        addNotification(message, userName);
                    }
                }          }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
*/

        //


        return START_STICKY;
    }


    private void addNotification(String msg, String userName, int senderCount) {

        ShortcutBadger.with(getApplicationContext()).count(count);
        if (msg.equals("")) {
            msg = "Image";
        }
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_image_black_24dp)
                        .setContentTitle("New Message")
                        .setContentText(userName + " : " + msg)
                        .setNumber(count);


        Notification.InboxStyle notification = new Notification.InboxStyle(builder);

        for (int i = 0; i < al_chat_list.size(); i++) {

            notification.addLine(al_chat_list.get(i).getSenderName() + ": " + al_chat_list.get(i).getMessage());
        }
        notification.setBigContentTitle("Chat App");
        notification.setSummaryText(al_chat_list.size() + " messages from " + senderCount + " chats");
        notification.build();

        Intent notificationIntent = new Intent(this, ChatActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       /* // Add as notification
         NotificationChannel channel = new NotificationChannel("123", "some_channel_id", NotificationManager.IMPORTANCE_LOW);

        channel.setShowBadge(true);
        manager.createNotificationChannel(channel);*/
        manager.notify(0, builder.build());
        count = count + 1;
    }

}

