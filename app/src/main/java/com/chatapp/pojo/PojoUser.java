package com.chatapp.pojo;

public class PojoUser {
    public  String startChat,isRead,msgCount,senderName,image,userId,name,password,phone,lastMsg,date;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PojoUser(String startChat,String isRead,String msgCount,String senderName,String name, String passwor, String phone, String userId,String image,String lastMsg,String date) {
        this.isRead=isRead;
        this.name = name;
        this.password = passwor;
        this.phone = phone;
        this.userId=userId;
        this.image=image;
        this.lastMsg=lastMsg;
        this.date=date;
        this.senderName=senderName;
        this.msgCount=msgCount;
        this.startChat=startChat;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswor() {
        return password;
    }

    public void setPasswor(String passwor) {
        this.password = passwor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getStartChat() {
        return startChat;
    }

    public void setStartChat(String startChat) {
        this.startChat = startChat;
    }
}
