package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
 
public class Researcher {
    private int researcherId;
    private String firstName;
    private String lastName;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private List<Message> messages;
 
    public Researcher(int researcherId, String firstName, String lastName) {
        this.researcherId = researcherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.messages = new ArrayList<>();
    }
 
    public int getResearcherId()         { return researcherId; }
    public String getFirstName()         { return firstName; }
    public String getLastName()          { return lastName; }
    public List<ResearchPaper> getPapers()    { return papers; }
    public List<ResearchProject> getProjects(){ return projects; }
    public List<Message> getMessages()   { return messages; }
 
    public int calculateHIndex() {
        List<Integer> c = new ArrayList<>();
        for (ResearchPaper p : papers) c.add(p.getCitations());
        c.sort(Comparator.reverseOrder());
        int h = 0;
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i) >= i + 1) h = i + 1;
            else break;
        }
        System.out.println("[Researcher] " + firstName + "'s h-index = " + h);
        return h;
    }
 
    public void publishResearchPaper(ResearchPaper paper) {
        paper.addAuthorName(firstName + " " + lastName);
        papers.add(paper);
        System.out.println("[Researcher] " + firstName + " published '" + paper.getTitle() + "'");
    }
 
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        System.out.println("[Researcher] Papers by " + firstName + ":");
        for (ResearchPaper p : sorted) {
            System.out.println("  - " + p.getTitle()
                    + " | citations: " + p.getCitations()
                    + " | pages: " + p.getPages());
        }
    }
 
    public void joinProject(ResearchProject project) {
        projects.add(project);
        project.addParticipant(firstName + " " + lastName);
    }
 
    public void getCitation(ResearchPaper paper, String format) {
        paper.getCitation(format);
    }
 
    public void sendMessage(Message message) {
        messages.add(message);
        System.out.println(firstName + " sent message to " + message.getReceiver());
    }
 
    public void receiveMessage(Message message) {
        messages.add(message);
        System.out.println(firstName + " received message from " + message.getSender());
    }
 
    public void viewMessages() {
        System.out.println("=== Messages for " + firstName + " ===");
        for (Message m : messages) {
            System.out.println("  [" + m.getDate() + "] " + m.getSender() + ": " + m.getContent());
        }
    }
 
    @Override
    public String toString() {
        return "Researcher{name=" + firstName + " " + lastName
                + ", papers=" + papers.size()
                + ", projects=" + projects.size() + "}";
    }

	public void addPaper(ResearchPaper paper) {
		
		
	}
}
