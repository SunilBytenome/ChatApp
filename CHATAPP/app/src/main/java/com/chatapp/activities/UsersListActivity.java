package com.chatapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chatapp.R;
import com.chatapp.adapter.AdapterUserList;
import com.chatapp.pojo.PojoUser;
import com.chatapp.utils.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UsersListActivity extends AppCompatActivity {
    RecyclerView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    ArrayList<PojoUser> al_user = new ArrayList<>();
    ArrayList<String> al_list = new ArrayList<>();
    private LinearLayoutManager manager;
    AdapterUserList adapterUserList;
    private ImageView ivBack;
    private TextView tvStatus;
    private String chatStart,image, date, lastMsg, senderName,msgCount,isRead;
    Firebase reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (RecyclerView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvStatus = findViewById(R.id.tvStatus);
        //ShortcutBadger.removeCount(getApplicationContext());
        reference1 = new Firebase("https://chatapp-9dacf.firebaseio.com/user");
        pd = new ProgressDialog(UsersListActivity.this);
        pd.setMessage("Loading...");
        pd.show();



        clicks();
    }

    private void clicks() {

       /* DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("user");

        ValueEventListener eventListener = new ValueEventListener() {


            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    totalUsers++;
                    String name = messageSnapshot.child("Detail").child("name").getValue().toString();

                    if (!Constants.username.equals(name)) {
                        String    userId= messageSnapshot.child("Detail").child("userId").getValue().toString();
                        image = messageSnapshot.child("Detail").child("image").getValue().toString();
                        date = messageSnapshot.child("Detail").child("date").getValue().toString();
                        lastMsg = messageSnapshot.child("Detail").child("lastMsg").getValue().toString();
                        senderName = messageSnapshot.child("Detail").child("senderName").getValue().toString();

                        PojoUser pojoUser = new PojoUser(senderName, name, "", "", userId, image, lastMsg, date);
                        al_user.add(pojoUser);
                    }

                }

                if (totalUsers <= 1) {
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                } else {
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);

                    Collections.sort(al_user, new Comparator<PojoUser>() {
                        public int compare(PojoUser o1, PojoUser o2) {
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });

                    adapterUserList = new AdapterUserList(UsersListActivity.this, al_user);
                    manager = new LinearLayoutManager(UsersListActivity.this);
                    usersList.setLayoutManager(manager);
                    usersList.setAdapter(adapterUserList);

                    //  usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
                }

                pd.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);*/
       reference1.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChildAdded(DataSnapshot messageSnapshot, String s) {
                messageSnapshot.child("Detail").child("phone").getValue();
                    totalUsers++;
                    String name = messageSnapshot.child("Detail").child("name").getValue().toString();
                   if (!Constants.username.equals(name)) {
                    String    userId= messageSnapshot.child("Detail").child("userId").getValue().toString();
                        image = messageSnapshot.child("Detail").child("image").getValue().toString();
                        date = messageSnapshot.child("Detail").child("date").getValue().toString();
                        lastMsg = messageSnapshot.child("Detail").child("lastMsg").getValue().toString();
                        senderName = messageSnapshot.child("Detail").child("senderName").getValue().toString();
                        try {
                            msgCount= messageSnapshot.child("Detail").child("msgCount").getValue().toString();
                            isRead= messageSnapshot.child("Detail").child("isRead").getValue().toString();
                            chatStart= messageSnapshot.child("Detail").child("chatStart").getValue().toString();

                            Log.e("msgCount",msgCount);
                        }catch (Exception e){

                        }

                        PojoUser pojoUser = new PojoUser(chatStart,isRead,msgCount,senderName, name, "", "", userId, image, lastMsg, date);
                      al_user.add(pojoUser);
                   }



                if (totalUsers <= 1) {
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                } else {
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);

                    Collections.sort(al_user, new Comparator<PojoUser>() {
                        public int compare(PojoUser o1, PojoUser o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });

                    Collections.reverse(al_user);
                    if (adapterUserList == null) {
                    adapterUserList = new AdapterUserList(UsersListActivity.this, al_user);
                    manager = new LinearLayoutManager(UsersListActivity.this);
                    usersList.setLayoutManager(manager);
                    usersList.setAdapter(adapterUserList);
                        pd.dismiss();
                    }else {
                        adapterUserList.notifyDataSetChanged();
                    }

                    //  usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
                }



            }

            @Override
            public void onChildChanged(DataSnapshot messageSnapshot, String s) {
                totalUsers++;
                String name = messageSnapshot.child("Detail").child("name").getValue().toString();


                    String    userId= messageSnapshot.child("Detail").child("userId").getValue().toString();
                    image = messageSnapshot.child("Detail").child("image").getValue().toString();
                    date = messageSnapshot.child("Detail").child("date").getValue().toString();
                    lastMsg = messageSnapshot.child("Detail").child("lastMsg").getValue().toString();
                    senderName = messageSnapshot.child("Detail").child("senderName").getValue().toString();
                    try {
                        msgCount= messageSnapshot.child("Detail").child("msgCount").getValue().toString();
                        isRead= messageSnapshot.child("Detail").child("isRead").getValue().toString();
                        chatStart= messageSnapshot.child("Detail").child("chatStart").getValue().toString();
                        Log.e("msgCount",msgCount);
                    }catch (Exception e){

                    }

                  /*  PojoUser pojoUser = new PojoUser(senderName, name, "", "", userId, image, lastMsg, date);
                    al_user.add(pojoUser);*/


                  for (int i=0;i<al_user.size();i++){
                     if (al_user.get(i).getName().equals(name)){
                          al_user.get(i).setName(name);
                          al_user.get(i).setSenderName(senderName);
                          al_user.get(i).setUserId(userId);
                          al_user.get(i).setImage(image);
                          al_user.get(i).setLastMsg(lastMsg);
                          al_user.get(i).setDate(date);
                          al_user.get(i).setMsgCount(msgCount);
                          al_user.get(i).setIsRead(isRead);
                    }
                  }
                if (totalUsers <= 1) {
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                } else {
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);

                    Collections.sort(al_user, new Comparator<PojoUser>() {
                        public int compare(PojoUser o1, PojoUser o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
Collections.reverse(al_user);

                    if (adapterUserList == null) {
                        adapterUserList = new AdapterUserList(UsersListActivity.this, al_user);
                        manager = new LinearLayoutManager(UsersListActivity.this);
                        usersList.setLayoutManager(manager);
                        usersList.setAdapter(adapterUserList);
                        pd.dismiss();
                    }else {
                        adapterUserList.notifyDataSetChanged();
                    }

                    //  usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
                }



                Map map = messageSnapshot.getValue(Map.class);
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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsersListActivity.this, UserStatusActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {

        String url = "https://chatapp-9dacf.firebaseio.com/user.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(UsersListActivity.this);
        rQueue.add(request);


        super.onResume();
    }

    public void doOnSuccess(String s) {
        al_user.clear();
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                totalUsers++;

                String name = obj.getJSONObject(key).getJSONObject("Detail").getString("name");

                if (!Constants.username.equals(name)) {


                    String userId = obj.getJSONObject(key).getJSONObject("Detail").getString("userId");

                    try {
                        image = obj.getJSONObject(key).getJSONObject("Detail").getString("image");
                        date = obj.getJSONObject(key).getJSONObject("Detail").getString("date");
                        lastMsg = obj.getJSONObject(key).getJSONObject("Detail").getString("lastMsg");
                        senderName = obj.getJSONObject(key).getJSONObject("Detail").getString("senderName");
                    } catch (Exception e) {
                    }
                    try {
                        image = obj.getJSONObject(key).getJSONObject("Detail").getString("image");
                        date = obj.getJSONObject(key).getJSONObject("Detail").getString("date");
                        lastMsg = obj.getJSONObject(key).getJSONObject("Detail").getString("lastMsg");
                        senderName = obj.getJSONObject(key).getJSONObject("Detail").getString("senderName");
                    } catch (Exception e) {
                    }
                    try {
                        image = obj.getJSONObject(key).getJSONObject("Detail").getString("image");
                        date = obj.getJSONObject(key).getJSONObject("Detail").getString("date");
                        lastMsg = obj.getJSONObject(key).getJSONObject("Detail").getString("lastMsg");
                        senderName = obj.getJSONObject(key).getJSONObject("Detail").getString("senderName");
                    } catch (Exception e) {
                    }
                    try {
                        image = obj.getJSONObject(key).getJSONObject("Detail").getString("image");
                        date = obj.getJSONObject(key).getJSONObject("Detail").getString("date");
                        lastMsg = obj.getJSONObject(key).getJSONObject("Detail").getString("lastMsg");
                        senderName = obj.getJSONObject(key).getJSONObject("Detail").getString("senderName");
                    } catch (Exception e) {
                    }
                   // PojoUser pojoUser = new PojoUser(senderName, name, "", "", userId, image, lastMsg, date);
                  //  al_user.add(pojoUser);


                }

                al.add(key);
            }


        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);

            Collections.sort(al_user, new Comparator<PojoUser>() {
                public int compare(PojoUser o1, PojoUser o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });

            adapterUserList = new AdapterUserList(UsersListActivity.this, al_user);
            manager = new LinearLayoutManager(UsersListActivity.this);
            usersList.setLayoutManager(manager);
            usersList.setAdapter(adapterUserList);

            //  usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }

}