package model;

import java.util.ArrayList;
import java.util.List;

public class GraduateStudent extends Student {
    private String researchTopic;

    private Object researchSupervisor;
    private List<Object> diplomaPapers;

    public GraduateStudent(String id, String name, String major, int yearOfStudy, String researchTopic) {
        super(id, name, major, yearOfStudy);
        this.researchTopic = researchTopic;
        this.diplomaPapers = new ArrayList<>();
    }

    public String getResearchTopic() {
        return researchTopic;
    }

    public Object getResearchSupervisor() {
        return researchSupervisor;
    }

    public List<Object> getDiplomaPapers() {
        return diplomaPapers;
    }

    public void setResearchTopic(String researchTopic) {
        this.researchTopic = researchTopic;
    }

    public void setResearchSupervisor(Object researchSupervisor) {
        this.researchSupervisor = researchSupervisor;
    }

    public void addDiplomaPaper(Object paper) {
        diplomaPapers.add(paper);
    }

    @Override
    public String toString() {
        return super.toString() + " | Graduate Student | Topic: " + researchTopic;
    }
}