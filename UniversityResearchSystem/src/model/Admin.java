package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Admin extends Employee implements Serializable {
    private int adminId;
    private List<User> users;
    private List<String> logs;

    public Admin(int userId, String firstName, String lastName,
                 String password, String email, int employeeId, int adminId) {
        super(userId, firstName, lastName, password, email, employeeId, "Admin");
        this.adminId = adminId;
        this.users = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    public int getAdminId() { return adminId; }

    public void addUser(User user) {
        users.add(user);
        String log = "[ADD] User added: " + user.getFirstName() + " " + user.getLastName();
        logs.add(log);
        System.out.println(log);
    }

    public void removeUser(User user) {
        boolean removed = users.remove(user);
        if (removed) {
            String log = "[REMOVE] User removed: " + user.getFirstName() + " " + user.getLastName();
            logs.add(log);
            System.out.println(log);
        } else {
            System.out.println("User not found: " + user.getFirstName());
        }
    }

    public void updateUser(User user, String newFirstName, String newLastName, String newEmail) {
        String log = "[UPDATE] User updated: " + user.getFirstName() + " -> " + newFirstName;
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setEmail(newEmail);
        logs.add(log);
        System.out.println(log);
    }

    public void viewUsers() {
        System.out.println("=== All Users ===");
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void viewLogs() {
        System.out.println("=== System Log Files ===");
        if (logs.isEmpty()) {
            System.out.println("No logs available.");
            return;
        }
        for (String log : logs) {
            System.out.println(log);
        }
    }

    public List<User> getUsers() { return users; }
    public List<String> getLogs() { return logs; }

    @Override
    public String toString() {
        return "Admin{id=" + adminId + ", name=" + getFirstName() + " " + getLastName() + "}";
    }
}
