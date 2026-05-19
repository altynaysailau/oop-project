package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * University research journal.
 * Uses Observer pattern: subscribers (Users) are notified when a new paper is published.
 */
public class Journal implements Serializable {
    private static final long serialVersionUID = 1L;

    private int journalId;
    private String name;
    private List<ResearchPaper> papers;
    private List<User> subscribers;   // Observer list

    public Journal(int journalId, String name) {
        this.journalId = journalId;
        this.name = name;
        this.papers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public int getJournalId()            { return journalId; }
    public String getName()              { return name; }
    public List<ResearchPaper> getPapers() { return papers; }
    public List<User> getSubscribers()   { return subscribers; }

    /** Subscribe a user to this journal (Observer pattern) */
    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            System.out.println("📧  " + user.getFirstName() + " subscribed to journal '" + name + "'");
        } else {
            System.out.println(user.getFirstName() + " is already subscribed to '" + name + "'");
        }
    }

    /** Unsubscribe */
    public void unsubscribe(User user) {
        subscribers.remove(user);
        System.out.println(user.getFirstName() + " unsubscribed from '" + name + "'");
    }

    /**
     * Publish a paper and notify all subscribers (Observer notify).
     */
    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("\n📰  New paper published in '" + name + "': " + paper.getTitle());
        notifySubscribers(paper);
    }

    private void notifySubscribers(ResearchPaper paper) {
        if (subscribers.isEmpty()) return;
        System.out.println("🔔  Notifying " + subscribers.size() + " subscriber(s):");
        for (User u : subscribers) {
            System.out.println("    → " + u.getFirstName() + " " + u.getLastName()
                    + ": New paper in '" + name + "' – " + paper.getTitle());
        }
    }

    @Override
    public String toString() {
        return "Journal{id=" + journalId + ", name='" + name
                + "', papers=" + papers.size()
                + ", subscribers=" + subscribers.size() + "}";
    }
}