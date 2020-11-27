package com.sean.TagMuh;

class Chat {

    private String receiverId,senderId,textMsg;
    private long sentTime;

    public Chat() {
    }

    public Chat(String receiverId, String senderId, String textMsg, long sentTime) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.textMsg = textMsg;
        this.sentTime = sentTime;
    }


    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }
}
