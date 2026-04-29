package model;

import enums.ProjectStatus;
import java.util.ArrayList;
import java.util.List;
 
public class ResearchProject {
    private int projectId;
    private String topic;
    private List<ResearchPaper> papers;
    private List<String> participantNames;
    private ProjectStatus status;
    
    public ResearchProject(int projectId, String topic) {
        this.projectId = projectId;
        this.topic = topic;
        this.papers = new ArrayList<>();
        this.participantNames = new ArrayList<>();
    }
 
    public int getProjectId()                  { return projectId; }
    public String getTopic()                   { return topic; }
    public List<ResearchPaper> getPapers()     { return papers; }
    public List<String> getParticipantNames()  { return participantNames; }
    public void setTopic(String topic)         { this.topic = topic; }
 
    public void addParticipant(String name) {
        participantNames.add(name);
        System.out.println("[Project] " + name + " joined '" + topic + "'");
    }
 
    public void removeParticipant(String name) {
        participantNames.remove(name);
        System.out.println("[Project] " + name + " left '" + topic + "'");
    }
 
    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("[Project] Paper '" + paper.getTitle() + "' added to '" + topic + "'");
    }
 
    @Override
    public String toString() {
        return "ResearchProject{id=" + projectId + ", topic='" + topic
                + "', participants=" + participantNames.size()
                + ", papers=" + papers.size() + "}";
    }

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}
}