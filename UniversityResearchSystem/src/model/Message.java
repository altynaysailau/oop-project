package model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private User sender;
    private User receiver;
    private String content;
    private Date date;

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }

    public User getSender() { return sender; }
    public User getReceiver() { return receiver; }
    public String getContent() { return content; }
    public Date getDate() { return date; }

    @Override
    public String toString() {
        return "[" + date + "] From: " + sender.getFirstName() + " -> " + receiver.getFirstName()
                + ": " + content;
    }
}
