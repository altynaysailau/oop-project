package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public abstract class User implements Serializable {
    private int userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;
    private Date registrationDate;
    private Date createdDate;
    private List<Message> messages;

    public User(int userId, String firstName, String lastName, String password, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.registrationDate = new Date();
        this.createdDate = new Date();
        this.messages = new ArrayList<>();
    }

    public boolean login(String password) {
        if (this.password.equals(password)) {
            System.out.println(firstName + " " + lastName + " logged in successfully.");
            return true;
        }
        System.out.println("Invalid password.");
        return false;
    }

    public void logout() {
        System.out.println(firstName + " " + lastName + " logged out.");
    }

    public void viewNews() {
        System.out.println(firstName + " is viewing news.");
    }

    public void sendMessage(Message message) {
        System.out.println(firstName + " sent a message to " + message.getReceiver().getFirstName());
    }

    public void receiveMessage(Message message) {
        messages.add(message);
    }

    public void viewMessages() {
        System.out.println("Messages for " + firstName + ":");
        for (Message m : messages) {
            System.out.println(m);
        }
    }

    // Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Date getRegistrationDate() { return registrationDate; }
    public Date getCreatedDate() { return createdDate; }

    public List<Message> getMessages() { return messages; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User user = (User) obj;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "[" + userId + "] " + firstName + " " + lastName + " <" + email + ">";
    }
}
