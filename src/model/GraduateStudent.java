package model;

import exceptions.LowHIndexException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraduateStudent extends Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MIN_SUPERVISOR_H_INDEX = 3;

    private String researchTopic;
    private Researcher researchSupervisor;          // typed as Researcher
    private List<ResearchPaper> diplomaPapers;      // typed as ResearchPaper

    public GraduateStudent(String id, String name, String major,
                           int yearOfStudy, String researchTopic) {
        super(id, name, major, yearOfStudy);
        this.researchTopic = researchTopic;
        this.diplomaPapers = new ArrayList<>();
    }

    public String getResearchTopic()            { return researchTopic; }
    public Researcher getResearchSupervisor()   { return researchSupervisor; }
    public List<ResearchPaper> getDiplomaPapers() { return diplomaPapers; }

    public void setResearchTopic(String t)      { this.researchTopic = t; }

    /**
     * Assign a supervisor. Throws LowHIndexException if supervisor's h-index < 3.
     */
    public void setResearchSupervisor(Researcher supervisor) throws LowHIndexException {
        int h = supervisor.calculateHIndex();
        if (h < MIN_SUPERVISOR_H_INDEX) {
            throw new LowHIndexException(
                supervisor.getFirstName() + " " + supervisor.getLastName()
                + " has h-index=" + h + " (< " + MIN_SUPERVISOR_H_INDEX
                + ") and cannot be assigned as a supervisor.");
        }
        this.researchSupervisor = supervisor;
        System.out.println("✅  Supervisor assigned: " + supervisor.getFirstName()
                + " (h=" + h + ") -> " + getName());
    }

    /**
     * Kept for Main.java backward compat where Object is passed.
     * Internally casts to Researcher and validates h-index.
     */
    public void setResearchSupervisor(Object supervisor) {
        if (supervisor instanceof Researcher r) {
            try {
                setResearchSupervisor(r);
            } catch (LowHIndexException e) {
                System.out.println("❌  " + e.getMessage());
            }
        } else {
            System.out.println("❌  Supervisor must be a Researcher.");
        }
    }

    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(paper);
        System.out.println("[GraduateStudent] Diploma paper added: " + paper.getTitle());
    }

    @Override
    public String toString() {
        return super.toString() + " | Graduate | Topic: " + researchTopic
                + " | Supervisor: " + (researchSupervisor != null
                    ? researchSupervisor.getFirstName() + " " + researchSupervisor.getLastName()
                    : "none");
    }
}