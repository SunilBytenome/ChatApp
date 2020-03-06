package com.chatapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chatapp.R;
import com.chatapp.adapter.AdapterChat;
import com.chatapp.pojo.PojoChat;
import com.chatapp.utils.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChatActivity extends AppCompatActivity {
    LinearLayout layout;
    ImageView ivSend, ivGallery;
    EditText etMessageArea;
    RecyclerView rvView;
    Firebase reference1, reference2,referenceUserMsg,referenceUserMsgCount;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    StorageReference storageReference;
    FirebaseStorage storage;
    ArrayList<PojoChat> al_arrray;
    AdapterChat adapterChat;
    private LinearLayoutManager manager;
    private ProgressDialog pd;
    private TextView tvStatus;
    public static int msgCount=0;
    public static  String isMsgRead = "";
    String userName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        pd = new ProgressDialog(ChatActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ivSend = (ImageView) findViewById(R.id.ivSend);
        etMessageArea = (EditText) findViewById(R.id.etMessageArea);
        rvView = (RecyclerView) findViewById(R.id.rvView);
        tvStatus=findViewById(R.id.tvStatus);
        al_arrray = new ArrayList<>();
        ivGallery = findViewById(R.id.ivGallery);
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chatapp-9dacf.firebaseio.com/messages/" + Constants.username + "_" + Constants.chatWith);
        reference2 = new Firebase("https://chatapp-9dacf.firebaseio.com/messages/" + Constants.chatWith + "_" + Constants.username);

        referenceUserMsg=new Firebase(Constants.userUpdateMsg+ Constants.username);
        referenceUserMsgCount=new Firebase(Constants.userUpdateMsg+ Constants.chatWith);

        referenceUserMsg.child("Detail").child("chatStart").setValue("yes");
        referenceUserMsgCount.child("Detail").child("msgCount").setValue(msgCount);
     //   referenceUserMsgCount.child("Detail").child("isRead").setValue("true");

        referenceUserMsgCount.child("Detail").child("isRead").setValue("true");
        referenceUserMsg.child("Detail").child("isRead").setValue("true");

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent, "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = etMessageArea.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();
                if (!messageText.equals("")) {
                    sendNewMessage(messageText,strDate);
                }
            }
        });
        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, UserStatusActivity.class));
            }
        });



        reference1.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);

                addNewNodeDatabase(map,dataSnapshot);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                Map map = dataSnapshot.getValue(Map.class);
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

        referenceUserMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {




            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                isMsgRead  = map.get("isRead").toString();
                int count= Integer.parseInt(map.get("msgCount").toString());
                msgCount=count;
                Log.e("msgCount", String.valueOf(msgCount));
/*
                userName = map.get("name").toString();
                if (!userName.equals(Constants.username)){
                    msgCount=0;
                }*/}
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage(); }
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference
                    .child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getDownloadUrl();

                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                    Date date = new Date();
                                    String strDate = dateFormat.format(date).toString();

                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("s_image", "");
                                    map.put("image", taskSnapshot.getDownloadUrl().toString());
                                    map.put("message", "");
                                    map.put("user", Constants.username);
                                    map.put("userId", Constants.userId);
                                    map.put("forward_count", String.valueOf(0));
                                    map.put("forward_mes","");
                                    map.put("time", String.valueOf(strDate));

                                    Map<String, String> map_two = new HashMap<String, String>();
                                    map_two.put("s_image", "");
                                    map_two.put("image", taskSnapshot.getDownloadUrl().toString());
                                    map_two.put("message", "");
                                    map_two.put("user", Constants.username);
                                    map_two.put("userId", Constants.partnerId);
                                    map_two.put("forward_count", String.valueOf(0));
                                    map_two.put("forward_mes","");
                                    map_two.put("time", String.valueOf(strDate));
                                    reference1.push().setValue(map);
                                    reference2.push().setValue(map_two);
                                    referenceUserMsg.child("Detail").child("lastMsg").setValue(taskSnapshot.getDownloadUrl().toString());
                                    referenceUserMsg.child("Detail").child("date").setValue(String.valueOf(strDate));
                                    referenceUserMsg.child("Detail").child("senderName").setValue(Constants.username);
                                    referenceUserMsg.child("Detail").child("chatWith").setValue(Constants.chatWith);
                                    if (isMsgRead.equals("true")){
                                        msgCount++;
                                    }else {
                                        //msgCount=0;
                                    }
                                    referenceUserMsg.child("Detail").child("msgCount").setValue(msgCount);
                                    progressDialog.dismiss();
                                    Toast.makeText(ChatActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(ChatActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivity.this);
        ImageView imageView = new ImageView(ChatActivity.this);
        //if (message.equals("")){

        Picasso.with(ChatActivity.this).load("http://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").into(imageView);
        // }else {
        textView.setText(message);
        // }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1.0f;
        lp.bottomMargin = 30;

        if (type == 1) {
            lp.gravity = Gravity.LEFT;
            //textView.setBackgroundResource(R.drawable.bubble_in);
            textView.setGravity(Gravity.LEFT);
            // imageView.setForegroundGravity(Gravity.LEFT);
            textView.setTextSize(17);
            textView.setTextColor(Color.BLUE);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            lp.gravity = Gravity.RIGHT;
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(16);
            // textView.setBackgroundResource(R.drawable.bubble_out);
            textView.setGravity(Gravity.RIGHT);
            // imageView.setForegroundGravity(Gravity.RIGHT);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        textView.setLayoutParams(lp);
        //   layout.addView(textView);
        //  layout.addView(imageView);
        //scrollView.fullScroll(View.FOCUS_DOWN);
    }

private void forwardMessage(String url,String forward_mes,int count,String key){
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    Date date = new Date();
    String strDate = dateFormat.format(date).toString();
    Map<String, String> map = new HashMap<String, String>();
    map.put("image", "");
    map.put("s_image", url);
    map.put("message", "");
    map.put("user", Constants.username);
    map.put("userId", Constants.userId);
    map.put("forward_count", "0");
    map.put("forward_mes",forward_mes);
    map.put("time", String.valueOf(strDate));

    Map<String, String> mapTwo = new HashMap<String, String>();
    mapTwo.put("image", "");
    mapTwo.put("s_image", url);
    mapTwo.put("message", "");
    mapTwo.put("user", Constants.username);
    mapTwo.put("userId", Constants.partnerId);
    mapTwo.put("forward_count","0");
    mapTwo.put("forward_mes",forward_mes);
    mapTwo.put("time", String.valueOf(strDate));

    reference1.push().setValue(map);
    reference2.push().setValue(mapTwo);
    referenceUserMsg.child("Detail").child("isRead").setValue("true");
    referenceUserMsg.child("Detail").child("lastMsg").setValue(url);
    referenceUserMsg.child("Detail").child("date").setValue(String.valueOf(strDate));
    referenceUserMsg.child("Detail").child("senderName").setValue(Constants.username);
    referenceUserMsg.child("Detail").child("chatWith").setValue(Constants.chatWith);
    if (isMsgRead.equals("true")){

        msgCount++;

    }else {
       // msgCount=0;
    }
    //referenceUserMsg.child("Detail").child("msgCount").setValue(msgCount);
    referenceUserMsg.child("Detail").child("msgCount").setValue(msgCount);
    reference1.child(key).child("forward_count").setValue(count);
    adapterChat.notifyDataSetChanged();
}

private void addNewNodeDatabase(Map map,DataSnapshot dataSnapshot){
    String time=map.get("time").toString();
    String message = map.get("message").toString();
  userName = map.get("user").toString();
    String Image = map.get("image").toString();
    String s_image = map.get("s_image").toString();
    String forward_count = map.get("forward_count").toString();
    String forward_mes= map.get("forward_mes").toString();

   String isMsgRead="";
    PojoChat pojoChat = new PojoChat();

    pojoChat.setMessage(message);
    pojoChat.setTime(time);
    pojoChat.setForward_mes(forward_mes);
    pojoChat.setS_image(s_image);
    pojoChat.setKey(dataSnapshot.getKey());
    pojoChat.setF_count(forward_count);
    pojoChat.setImage(Image);
    if (userName.equals(Constants.username)) {

        pojoChat.setUserName("You");
    } else {
        pojoChat.setUserName(Constants.chatWith);

    }
    al_arrray.add(pojoChat);
    rvView.scrollToPosition(al_arrray.size() - 1);
    if (adapterChat == null) {
        adapterChat = new AdapterChat(ChatActivity.this, al_arrray, new AdapterChat.InterfaceClick() {
            @Override
            public void sendImage(String forward_mes,String url,int count,String key) {
                forwardMessage(url,forward_mes,count,key);
            }
        });
        manager = new LinearLayoutManager(ChatActivity.this);
        rvView.setLayoutManager(manager);
        rvView.setAdapter(adapterChat);
        pd.dismiss();
    } else {
        adapterChat.notifyDataSetChanged();
    }
}

private void sendNewMessage(String messageText,String strDate){
    Map<String, String> map = new HashMap<String, String>();
    map.put("s_image", "");
    map.put("image", "");
    map.put("message", messageText);
    map.put("user", Constants.username);
    map.put("userId", Constants.userId);
    map.put("forward_count", String.valueOf(0));
    map.put("forward_mes","");
    map.put("time", String.valueOf(strDate));


    Map<String, String> map_two = new HashMap<String, String>();
    map_two.put("s_image", "");
    map_two.put("image", "");
    map_two.put("message", messageText);
    map_two.put("user", Constants.username);
    map_two.put("userId", Constants.partnerId);
    map_two.put("forward_count", String.valueOf(0));
    map_two.put("time", String.valueOf(strDate));

    if (isMsgRead.equals("true")){

        msgCount++;

    }else {
        //msgCount=0;
    }
    map_two.put("forward_mes","");
    referenceUserMsg.child("Detail").child("isRead").setValue("true");
    referenceUserMsg.child("Detail").child("lastMsg").setValue(messageText);
    referenceUserMsg.child("Detail").child("date").setValue(String.valueOf(strDate));
    referenceUserMsg.child("Detail").child("senderName").setValue(Constants.username);
    referenceUserMsg.child("Detail").child("chatWith").setValue(Constants.chatWith);

  //  referenceUserMsg.child("Detail").child("msgCount").setValue(msgCount);
    referenceUserMsg.child("Detail").child("msgCount").setValue(msgCount);
    reference1.push().setValue(map);
    reference2.push().setValue(map_two);
    etMessageArea.setHint("Write a message..");
    etMessageArea.setText("");
}


    @Override
    protected void onPause() {


        //referenceUserMsgCount.child("Detail").child("isRead").setValue("flase");

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        int count=0;
        referenceUserMsg.child("Detail").child("msgCount").setValue(msgCount);

        if (userName.equals(Constants.username)){

            referenceUserMsgCount.child("Detail").child("msgCount").setValue(count);

        }


        referenceUserMsg.child("Detail").child("isRead").setValue("flase");
        referenceUserMsg.child("Detail").child("chatStart").setValue("no");

        super.onBackPressed();
    }
}
