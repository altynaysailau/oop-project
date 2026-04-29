package model;

import enums.RequestStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TechSupporter extends Employee implements Serializable {
    private int supportId;
    private List<TechRequest> assignedRequests;

    public TechSupporter(int userId, String firstName, String lastName,
                         String password, String email, int employeeId, int supportId) {
        super(userId, firstName, lastName, password, email, employeeId, "Tech Supporter");
        this.supportId = supportId;
        this.assignedRequests = new ArrayList<>();
    }

    public int getSupportId() { return supportId; }

    /**
     * View details of a request; sets status to VIEWED automatically
     */
    public void viewRequest(TechRequest request) {
        if (request.getStatus() == RequestStatus.NEW) {
            request.updateStatus(RequestStatus.VIEWED);
        }
        request.viewDetails();
    }

    /**
     * View all new requests
     */
    public void viewNewRequests(List<TechRequest> allRequests) {
        System.out.println("=== New Tech Requests ===");
        boolean found = false;
        for (TechRequest r : allRequests) {
            if (r.getStatus() == RequestStatus.NEW || r.getStatus() == RequestStatus.VIEWED) {
                r.viewDetails();
                System.out.println("---");
                found = true;
            }
        }
        if (!found) System.out.println("No new requests.");
    }

    /**
     * Accept a request
     */
    public void acceptRequest(TechRequest request) {
        if (request.getStatus() == RequestStatus.NEW || request.getStatus() == RequestStatus.VIEWED) {
            request.updateStatus(RequestStatus.ACCEPTED);
            assignedRequests.add(request);
            System.out.println(getFirstName() + " accepted request #" + request.getRequestId());
        } else {
            System.out.println("Request #" + request.getRequestId()
                    + " cannot be accepted. Current status: " + request.getStatus());
        }
    }

    /**
     * Reject a request
     */
    public void rejectRequest(TechRequest request) {
        if (request.getStatus() != RequestStatus.DONE) {
            request.updateStatus(RequestStatus.REJECTED);
            System.out.println(getFirstName() + " rejected request #" + request.getRequestId());
        } else {
            System.out.println("Request #" + request.getRequestId() + " is already DONE.");
        }
    }

    /**
     * Mark a request as done
     */
    public void completeRequest(TechRequest request) {
        if (request.getStatus() == RequestStatus.ACCEPTED) {
            request.updateStatus(RequestStatus.DONE);
            System.out.println(getFirstName() + " completed request #" + request.getRequestId());
        } else {
            System.out.println("Request #" + request.getRequestId()
                    + " must be ACCEPTED before completing. Current: " + request.getStatus());
        }
    }

    public List<TechRequest> getAssignedRequests() { return assignedRequests; }

    @Override
    public String toString() {
        return "TechSupporter{id=" + supportId + ", name=" + getFirstName() + " " + getLastName() + "}";
    }
}
