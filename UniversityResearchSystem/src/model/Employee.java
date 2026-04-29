package model;

import java.io.Serializable;

public abstract class Employee extends User implements Serializable {
    private int employeeId;
    private String position;

    // Constructor matching Person 1's Teacher/existing classes: (firstName, employeeId, position)
    public Employee(String firstName, String employeeId, String position) {
        super(0, firstName, "", "password", "");
        try {
            this.employeeId = Integer.parseInt(employeeId);
        } catch (NumberFormatException e) {
            this.employeeId = employeeId.hashCode();
        }
        this.position = position;
    }

    // Constructor for our new classes: full User fields
    public Employee(int userId, String firstName, String lastName,
                    String password, String email, int employeeId, String position) {
        super(userId, firstName, lastName, password, email);
        this.employeeId = employeeId;
        this.position = position;
    }

    public int getEmployeeIdInt() { return employeeId; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public void sendMessageToEmployee(Employee recipient, String content) {
        Message msg = new Message(this, recipient, content);
        this.sendMessage(msg);
        recipient.receiveMessage(msg);
    }

    @Override
    public String toString() {
        return super.toString() + " | Position: " + position;
    }
}
