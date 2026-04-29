package model;

import java.io.Serializable;
import java.util.*;


public class Student implements Serializable, Comparable<Student> {
    private String id;
    private String name;
    private String major;
    private int yearOfStudy;
    private List<Course> courses = new ArrayList<>();
    private Map<Course, List<Mark>> marks = new HashMap<>();


    public Student(String id, String name, String major, int yearOfStudy) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
    }

    public String getId()         { return id; }
    public String getName()       { return name; }
    public String getMajor()      { return major; }
    public int getYearOfStudy()   { return yearOfStudy; }
    public List<Course> getCourses() { return courses; }

    public int getTotalCredits() {
        return courses.stream().mapToInt(Course::getCredits).sum();
    }

    public void addMark(Course course, Mark mark) {
        marks.computeIfAbsent(course, k -> new ArrayList<>()).add(mark);
    }

    public void viewMarks() {
        System.out.println("Marks of " + name + ":");
        for (Map.Entry<Course, List<Mark>> e : marks.entrySet()) {
            System.out.println(e.getKey().getCourseName() + " -> " + e.getValue());
        }
    }

    @Override public int compareTo(Student other) { return this.name.compareTo(other.name); }

    @Override public String toString() {
        return id + " | " + name + " | " + major + " | Year " + yearOfStudy;
    }

    @Override public boolean equals(Object obj) {
        if (!(obj instanceof Student)) return false;
        return id.equals(((Student) obj).id);
    }

    @Override public int hashCode() { return Objects.hash(id); }
}
