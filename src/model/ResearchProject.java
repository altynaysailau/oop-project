package model;

import enums.ProjectStatus;
import exceptions.NotAResearcherException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private int projectId;
    private String topic;
    private List<ResearchPaper> papers;
    private List<Researcher> participants;   // typed as Researcher now
    private ProjectStatus status;

    public ResearchProject(int projectId, String topic) {
        this.projectId = projectId;
        this.topic = topic;
        this.papers = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.status = ProjectStatus.IN_PROGRESS;
    }

    public int getProjectId()                    { return projectId; }
    public String getTopic()                     { return topic; }
    public List<ResearchPaper> getPapers()       { return papers; }
    public List<Researcher> getParticipants()    { return participants; }
    public ProjectStatus getStatus()             { return status; }
    public void setTopic(String topic)           { this.topic = topic; }
    public void setStatus(ProjectStatus status)  { this.status = status; }

    /**
     * Only a Researcher object may join. Throws NotAResearcherException otherwise.
     * Use this when you have an Object that might not be a Researcher.
     */
    public void joinProject(Object person) throws NotAResearcherException {
        if (!(person instanceof Researcher)) {
            throw new NotAResearcherException(
                person + " is not a Researcher and cannot join project '" + topic + "'.");
        }
        Researcher r = (Researcher) person;
        addParticipant(r);
    }

    /** Typed overload – no exception needed */
    public void addParticipant(Researcher r) {
        participants.add(r);
        System.out.println("[Project] " + r.getFirstName() + " " + r.getLastName()
                + " joined '" + topic + "'");
    }

    /** Legacy string-based helper kept for backward compat with existing Researcher.joinProject */
    public void addParticipant(String name) {
        System.out.println("[Project] " + name + " joined '" + topic + "'");
    }

    public void removeParticipant(Researcher r) {
        participants.remove(r);
        System.out.println("[Project] " + r.getFirstName() + " left '" + topic + "'");
    }

    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("[Project] Paper '" + paper.getTitle() + "' added to '" + topic + "'");
    }

    @Override
    public String toString() {
        return "ResearchProject{id=" + projectId + ", topic='" + topic
                + "', participants=" + participants.size()
                + ", papers=" + papers.size()
                + ", status=" + status + "}";
    }
}