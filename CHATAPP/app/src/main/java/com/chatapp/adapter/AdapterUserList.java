package com.chatapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chatapp.R;
import com.chatapp.activities.ChatActivity;
import com.chatapp.databinding.DialogImageBinding;
import com.chatapp.pojo.PojoUser;
import com.chatapp.service.ServiceChat;
import com.chatapp.utils.Constants;
import com.chatapp.utils.SharedPreferenceWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUserList extends RecyclerView.Adapter<AdapterUserList.Holder> {
    LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<PojoUser> al_user;
    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");


    public AdapterUserList(Activity activity, ArrayList<PojoUser> al_user) {
        this.activity = activity;
        this.al_user = al_user;
        layoutInflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = layoutInflater.inflate(R.layout.design_user, viewGroup, false);
        Holder vh = new Holder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final PojoUser pojoUser = al_user.get(i);


        /* if (!Constants.username.equals(al_user.get(i).getName())) {
             holder.llLayout.setVisibility(View.GONE);

         }else {*/
             holder.llLayout.setVisibility(View.VISIBLE);
             holder.tvMsgCounter.setVisibility(View.GONE);
             if (!pojoUser.getMsgCount().equals("0")){
                 holder.tvMsgCounter.setVisibility(View.VISIBLE);
                 holder.tvMsgCounter.setText(pojoUser.getMsgCount());
             }
             holder.tvName.setText(pojoUser.getName());
             if (pojoUser.getLastMsg().contains("https")) {
                 holder.tvLastMsg.setText(pojoUser.getSenderName() + " ");
                 holder.ivImage.setVisibility(View.VISIBLE);
             } else {
                 holder.ivImage.setVisibility(View.GONE);
                 holder.tvLastMsg.setText(pojoUser.getSenderName() + " : " + pojoUser.getLastMsg());
             }
             Glide.with(activity)
                     .load(pojoUser.getImage())
                     .centerCrop()
                     .placeholder(R.drawable.user)
                     .into(holder.circleImageView);
             holder.llLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     holder.tvMsgCounter.setVisibility(View.GONE);
                     Constants.chatWith = pojoUser.getName();
                     Constants.partnerId = pojoUser.getUserId();
                     activity.startService(new Intent(activity, ServiceChat.class));
                     SharedPreferenceWriter.getInstance(activity).writeStringValue("chatWith", Constants.chatWith);
                     SharedPreferenceWriter.getInstance(activity).writeStringValue("username", Constants.username);
                     activity.startActivity(new Intent(activity, ChatActivity.class));
                     }});
              try {
                 String[] splited = pojoUser.getDate().split("\\s+");
                 String date = splited[0];
                 String time = splited[1];
                 Date mDate = null;
                 try {
                     mDate = inFormat.parse(date);
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
                 String messageDay = outFormat.format(mDate);
                 Date currentDate = Calendar.getInstance().getTime();
                 System.out.println("Current time => " + currentDate);
                 String formattedDate = inFormat.format(currentDate);
                 Date mDateC = null;
                 try {
                     mDateC = inFormat.parse(formattedDate);
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
                 String currentDay = outFormat.format(mDateC);
                 if (currentDay.equals(messageDay)) {
                     messageDay = "Today";
                 }
                 holder.tvDate.setText(messageDay + "  " + time);
             } catch (Exception e) { }
             holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     dialog(pojoUser.getImage(), activity);
                 }
             });

       //  }

    }

    @Override
    public int getItemCount() {
        return al_user.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvMsgCounter, tvName, tvLastMsg, tvDate;
        CircleImageView circleImageView;
        LinearLayout llLayout;
        ImageView ivImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvMsgCounter = itemView.findViewById(R.id.tvMsgCounter);
            tvName = itemView.findViewById(R.id.tvName);
            tvLastMsg = itemView.findViewById(R.id.tvLastMsg);
            tvDate = itemView.findViewById(R.id.tvDate);
            circleImageView = itemView.findViewById(R.id.cvImage);
            ivImage = itemView.findViewById(R.id.ivImage);
            llLayout = itemView.findViewById(R.id.llLayout);
        }
    }

    private void dialog(String image, Activity activity) {

        DialogImageBinding dialogBind;
        dialogBind = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_image, null, false);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogBind.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        ImageView imageView = dialog.findViewById(R.id.ivImageView);
        ImageView ivBack = dialog.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(activity).load(image).placeholder(R.drawable.ic_camera_alt_black_24dp).into(imageView);

        dialog.show();
    }
}
