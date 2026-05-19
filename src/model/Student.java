package model;

import enums.MarkType;
import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;
import exceptions.MaxFailsExceededException;

import java.io.Serializable;
import java.util.*;

public class Student implements Serializable, Comparable<Student> {
    private static final long serialVersionUID = 1L;
    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS   = 3;
    private static final double PASS_SCORE = 50.0;

    private String id;
    private String name;
    private String major;
    private int yearOfStudy;
    private String password = "pass";
    private int failCount = 0;
    private List<Course> courses = new ArrayList<>();
    private Map<Course, List<Mark>> marks = new HashMap<>();

    public Student(String id, String name, String major, int yearOfStudy) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
    }

    public String getId()            { return id; }
    public String getName()          { return name; }
    public String getMajor()         { return major; }
    public int getYearOfStudy()      { return yearOfStudy; }
    public String getPassword()      { return password; }
    public void setPassword(String p){ this.password = p; }
    public List<Course> getCourses() { return courses; }
    public int getFailCount()        { return failCount; }

    public int getTotalCredits() {
        return courses.stream().mapToInt(Course::getCredits).sum();
    }

    public void registerForCourse(Course course)
            throws CreditLimitException, CourseAlreadyRegisteredException {
        if (courses.contains(course))
            throw new CourseAlreadyRegisteredException(
                name + " is already registered for: " + course.getCourseName());
        int newTotal = getTotalCredits() + course.getCredits();
        if (newTotal > MAX_CREDITS)
            throw new CreditLimitException(
                name + " cannot register for '" + course.getCourseName()
                + "'. Would exceed " + MAX_CREDITS + " credits ("
                + getTotalCredits() + " + " + course.getCredits() + ").");
        courses.add(course);
        course.addStudent(this);
    }


public void addMark(Course course, Mark mark) throws MaxFailsExceededException {
        if (mark.getType() == MarkType.FINAL && mark.getScore() < PASS_SCORE) {
            if (failCount >= MAX_FAILS)
                throw new MaxFailsExceededException(
                    name + " has already failed " + MAX_FAILS + " times. Academic dismissal required.");
            failCount++;
            System.out.println("⚠️  Fail recorded for " + name
                + ". Total fails: " + failCount + "/" + MAX_FAILS);
        }
        marks.computeIfAbsent(course, k -> new ArrayList<>()).add(mark);
    }

    public void viewMarks() {
        System.out.println("Marks of " + name + ":");
        if (marks.isEmpty()) { System.out.println("  (no marks yet)"); return; }
        for (Map.Entry<Course, List<Mark>> e : marks.entrySet())
            System.out.println("  " + e.getKey().getCourseName() + " -> " + e.getValue());
    }

    public void viewTranscript() {
        System.out.println("\n════════════ TRANSCRIPT ════════════");
        System.out.println("Student : " + name + "  (" + id + ")");
        System.out.println("Major   : " + major);
        System.out.println("Year    : " + yearOfStudy);
        System.out.println("────────────────────────────────────");
        System.out.printf("%-25s %-6s %-30s%n", "Course", "Cr", "Marks");
        System.out.println("─".repeat(65));
        double totalWeighted = 0; int totalCr = 0;
        for (Course c : courses) {
            List<Mark> m = marks.getOrDefault(c, Collections.emptyList());
            // compute weighted total: att1*0.3 + att2*0.3 + final*0.4
            double att1=0, att2=0, fin=0;
            for (Mark mk : m) {
                switch (mk.getType()) {
                    case FIRST_ATTESTATION  -> att1 = mk.getScore();
                    case SECOND_ATTESTATION -> att2 = mk.getScore();
                    case FINAL              -> fin  = mk.getScore();
                }
            }
            double total = att1*0.3 + att2*0.3 + fin*0.4;
            String grade = total >= 90 ? "A" : total >= 75 ? "B" : total >= 60 ? "C"
                         : total >= 50 ? "D" : !m.isEmpty() ? "F" : "—";
            totalWeighted += total * c.getCredits();
            totalCr += c.getCredits();
            System.out.printf("%-25s %-6d total=%-5.1f grade=%s%n",
                c.getCourseName(), c.getCredits(), total, grade);
        }
        System.out.println("─".repeat(65));
        double gpa = totalCr == 0 ? 0 : totalWeighted / totalCr / 25.0; // rough GPA/4
        System.out.printf("GPA (approx)          : %.2f / 4.0%n", Math.min(gpa, 4.0));
        System.out.println("Total credits enrolled: " + getTotalCredits() + " / " + MAX_CREDITS);
        System.out.println("Failed courses        : " + failCount + " / " + MAX_FAILS);
        System.out.println("════════════════════════════════════\n");
    }

    @Override public int compareTo(Student other) { return this.name.compareTo(other.name); }
    @Override public String toString() { return id + " | " + name + " | " + major + " | Year " + yearOfStudy; }
    @Override public boolean equals(Object obj) {
        if (!(obj instanceof Student)) return false;
        return id.equals(((Student) obj).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}