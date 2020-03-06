package com.chatapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chatapp.R;
import com.chatapp.databinding.DialogImageBinding;
import com.chatapp.pojo.PojoChat;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.Holder> {
    Activity context;
    ArrayList<PojoChat> al_arrray;
    LayoutInflater layoutInflater;
    InterfaceClick interfaceClick;
    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
    public AdapterChat(Activity activity, ArrayList<PojoChat> al_arrray, InterfaceClick interfaceClick) {
        this.al_arrray = al_arrray;
        this.context = activity;

        this.interfaceClick = interfaceClick;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View itemView = layoutInflater.inflate(R.layout.design_chat, viewGroup, false);
        Holder vh = new Holder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final PojoChat pojoChat = al_arrray.get(i);

        String[] splited =  pojoChat.getTime().split("\\s+");
        String date=splited[0];
        String time=splited[1];

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
        if (currentDay.equals(messageDay)){
            messageDay="Today";
        }

       /* Sender side*/
        if (pojoChat.getUserName().equals("You")) {
            holder.tvRSenderName.setVisibility(View.GONE);
            holder.ivRQuote.setVisibility(View.GONE);
            holder.llTop.setVisibility(View.VISIBLE);
            holder.llBottom.setVisibility(View.GONE);
            holder.tvRText.setText("You :-");
            pojoChat.getTime();

            holder.tvRTime.setText(messageDay+" "+time);
            holder.tvSRName.setText(pojoChat.getUserName());
            if (!pojoChat.getS_image().equals("")) {
                holder.ivRImage.setVisibility(View.GONE);
                holder.tvRMessage.setVisibility(View.GONE);
                holder.rlTopStatus.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(pojoChat.getS_image())
                        .placeholder(R.drawable.bubble_in)
                        .into(holder.ivSRImage);
            } else {
                holder.rlTopStatus.setVisibility(View.GONE);
                if (pojoChat.getImage().equals("")) {
                    holder.ivRImage.setVisibility(View.GONE);
                    holder.tvRMessage.setVisibility(View.VISIBLE);
                    holder.tvRMessage.setText(pojoChat.getMessage());
                    if (!pojoChat.getForward_mes().equals("")){
                        holder.tvRSenderName.setVisibility(View.VISIBLE);
                        holder.tvRMessage.setText(pojoChat.getForward_mes());
                        holder.tvRSenderName.setText(pojoChat.getUserName());
                        holder.ivRQuote.setVisibility(View.VISIBLE);
                    }
                } else {

                    holder.ivRImage.setVisibility(View.VISIBLE);
                    holder.tvRMessage.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(pojoChat.getImage())
                            .centerCrop()
                            .placeholder(R.drawable.bubble_in)
                            .into(holder.ivRImage);
                }
            }
        }   /* Reciver side*/
        else {
            holder.ivLQuote.setVisibility(View.GONE);
            holder.tvLSenderName.setVisibility(View.GONE);
            holder.llBottom.setVisibility(View.VISIBLE);
            holder.llTop.setVisibility(View.GONE);
            holder.tvLText.setText(pojoChat.userName + " :-");
            holder.tvLTime.setText(messageDay+" "+time);
            holder.tvSLName.setText(pojoChat.getUserName());
            if (!pojoChat.getS_image().equals("")) {
                holder.rlBottomStatus.setVisibility(View.VISIBLE);
                holder.ivLImage.setVisibility(View.GONE);
                holder.tvLMessage.setVisibility(View.GONE);
                Glide.with(context)
                        .load(pojoChat.getS_image())
                        .placeholder(R.drawable.bubble_in)
                        .into(holder.ivSLImage);
            } else {
                holder.rlBottomStatus.setVisibility(View.GONE);
                if (pojoChat.getImage().equals("")) {
                    holder.ivLImage.setVisibility(View.GONE);
                    holder.tvLMessage.setVisibility(View.VISIBLE);
                    holder.tvLMessage.setText(pojoChat.getMessage());
                    if (!pojoChat.getForward_mes().equals("")){
                        holder.tvLSenderName.setVisibility(View.VISIBLE);
                        holder.tvLMessage.setText(pojoChat.getForward_mes());
                        holder.tvLSenderName.setText(pojoChat.getUserName());
                        holder.ivLQuote.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.ivLImage.setVisibility(View.VISIBLE);
                    holder.tvLMessage.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(pojoChat.getImage())
                            .centerCrop()
                            .placeholder(R.drawable.bubble_in)
                            .into(holder.ivLImage); }
            } }
        holder.ivLImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.ivLForward.setVisibility(View.VISIBLE);
                holder.llLImage.setBackgroundColor(Color.parseColor("#2A646363"));
                return true;
            }
        });
        holder.ivRImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              //  interfaceClick.sendImage(pojoChat.getImage());
                holder.ivRForward.setVisibility(View.VISIBLE);
                holder.llRImage.setBackgroundColor(Color.parseColor("#2A646363"));
                return true;
            }
        });
        holder.ivRForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivRForward.setVisibility(View.GONE);
                holder.llRImage.setBackgroundColor(Color.TRANSPARENT);
                interfaceClick.sendImage("",pojoChat.getImage(),1,pojoChat.getKey());;
            }
        });

        holder.ivLForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count= Integer.parseInt(pojoChat.getF_count());
                count= count+1;
                holder.llLImage.setBackgroundColor(Color.TRANSPARENT);
                interfaceClick.sendImage("",pojoChat.getImage(),count,pojoChat.getKey());
                holder.ivLForward.setVisibility(View.GONE);

            }
        });
        holder.llRMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgForward;
                if (pojoChat.getMessage().equals("")){
                    msgForward=pojoChat.getForward_mes();
                }else {
                    msgForward=pojoChat.getMessage();
                }
                interfaceClick.sendImage(msgForward,pojoChat.getImage(),0,pojoChat.getKey());
            }
        });
        holder.llLMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgForward;
                if (pojoChat.getMessage().equals("")){
                    msgForward=pojoChat.getForward_mes();
                }else {
                    msgForward=pojoChat.getMessage();
                }
                interfaceClick.sendImage(msgForward,pojoChat.getImage(),0,pojoChat.getKey());
        }
        });
           /*   holder.ivLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count= Integer.parseInt(pojoChat.getF_count());
                count= count+1;
                interfaceClick.sendImage("",pojoChat.getImage(),count,pojoChat.getKey());
                holder.ivLForward.setVisibility(View.GONE);
                holder.llLImage.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        holder.ivRImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivRForward.setVisibility(View.GONE);
                holder.llRImage.setBackgroundColor(Color.TRANSPARENT);
              //  holder.llRImage.setSelected(false);
            }
        });*/
    /*    holder.ivRForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivRForward.setVisibility(View.GONE);
                int count= Integer.parseInt(pojoChat.getF_count());
                count= count+1;
                interfaceClick.sendImage("",pojoChat.getImage(),count,pojoChat.getKey());;
                Toast.makeText(context, "ivRForward", Toast.LENGTH_SHORT).show();
            }
        });

        holder.ivLForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivLForward.setVisibility(View.GONE);
                interfaceClick.sendImage("",pojoChat.getImage(),1,pojoChat.getKey());
                Toast.makeText(context, "ivLForward", Toast.LENGTH_SHORT).show();
            }
        });*/

    holder.ivRImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog(pojoChat.getImage(),context);
        }
    });

        holder.ivLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(pojoChat.getImage(),context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al_arrray.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout llTop, llBottom,llRMessageSend,llLMessageSend;
        RelativeLayout llRImage,llLImage;
        RoundedImageView ivRImage,ivLImage;
        ImageView  ivSRImage, ivSLImage,ivRForward,ivLForward,ivRQuote,ivLQuote;
        TextView tvRTime,tvLTime,tvRText, tvSLName, tvSRName, tvRMessage, tvLText, tvLMessage,tvLSenderName,tvRSenderName;
        RelativeLayout rlTopStatus, rlBottomStatus;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvRText = itemView.findViewById(R.id.tvRText);
            tvRMessage = itemView.findViewById(R.id.tvRMessage);
            tvLText = itemView.findViewById(R.id.tvLText);
            tvLMessage = itemView.findViewById(R.id.tvLMessage);
            llRMessageSend = itemView.findViewById(R.id.llRMessageSend);
            llLMessageSend = itemView.findViewById(R.id.llLMessageSend);
            tvSLName = itemView.findViewById(R.id.tvSLName);
            tvSRName = itemView.findViewById(R.id.tvSRName);

            tvRTime = itemView.findViewById(R.id.tvRTime);
            tvLTime = itemView.findViewById(R.id.tvLTime);

            tvLSenderName = itemView.findViewById(R.id.tvLSenderName);
            tvRSenderName = itemView.findViewById(R.id.tvRSenderName);

            llTop = itemView.findViewById(R.id.llTop);
            llBottom = itemView.findViewById(R.id.llBottom);
            llRImage = itemView.findViewById(R.id.llRImage);
            llLImage = itemView.findViewById(R.id.llLImage);
            ivLForward = itemView.findViewById(R.id.ivLForward);
            ivRForward = itemView.findViewById(R.id.ivRForward);
            ivRImage = itemView.findViewById(R.id.ivRImage);
            ivLImage = itemView.findViewById(R.id.ivLImage);
            ivSLImage = itemView.findViewById(R.id.ivSLImage);
            ivSRImage = itemView.findViewById(R.id.ivSRImage);

            ivRQuote = itemView.findViewById(R.id.ivRQuote);
            ivLQuote = itemView.findViewById(R.id.ivLQuote);

            rlBottomStatus = itemView.findViewById(R.id.rlBottomStatus);
            rlTopStatus = itemView.findViewById(R.id.rlTopStatus);
        }
    }

  public   interface InterfaceClick {
        void sendImage(String message,String url,int count,String key);
    }


    private void dialog(String image,Activity activity){

        DialogImageBinding dialogBind;
        dialogBind = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_image, null, false);

        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogBind.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        ImageView imageView=dialog.findViewById(R.id.ivImageView);
        ImageView ivBack=dialog.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(activity).load(image).placeholder(R.drawable.ic_camera_alt_black_24dp).into( imageView);

        dialog.show();
    }
}
