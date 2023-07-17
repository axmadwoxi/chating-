package com.example.chating;

public class Message_model {
    static String SENT_BY_ME = "me";
    static String SENT_BY_Bot = "bot";
    String message;
    String sentBy;

    public Message_model(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
