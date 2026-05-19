package model;

import enums.RequestStatus;
import enums.Urgency;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TechRequest implements Serializable {
    private int requestId;
    private String description;
    private RequestStatus status;
    private Urgency urgency;
    private User createdBy;
    private Date createdDate;

    public TechRequest(int requestId, String description, Urgency urgency, User createdBy) {
        this.requestId = requestId;
        this.description = description;
        this.urgency = urgency;
        this.createdBy = createdBy;
        this.status = RequestStatus.NEW;
        this.createdDate = new Date();
    }

    public int getRequestId() { return requestId; }
    public String getDescription() { return description; }
    public RequestStatus getStatus() { return status; }
    public Urgency getUrgency() { return urgency; }
    public User getCreatedBy() { return createdBy; }
    public Date getCreatedDate() { return createdDate; }

    public void updateStatus(RequestStatus newStatus) {
        System.out.println("Request #" + requestId + " status: " + this.status + " -> " + newStatus);
        this.status = newStatus;
    }

    public void viewDetails() {
        System.out.println("=== Tech Request #" + requestId + " ===");
        System.out.println("Description : " + description);
        System.out.println("Urgency     : " + urgency);
        System.out.println("Status      : " + status);
        System.out.println("Created by  : " + createdBy.getFirstName() + " " + createdBy.getLastName());
        System.out.println("Date        : " + createdDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TechRequest)) return false;
        TechRequest r = (TechRequest) obj;
        return requestId == r.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    @Override
    public String toString() {
        return "TechRequest{#" + requestId + ", urgency=" + urgency + ", status=" + status + "}";
    }
}
