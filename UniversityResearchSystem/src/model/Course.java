package model;

import enums.CourseType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private String courseCode;
    private String courseName;
    private int credits;
    private CourseType courseType;
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers  = new ArrayList<>();

    // Matches Person 1's constructor exactly
    public Course(String courseCode, String courseName, int credits, CourseType courseType) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.courseType = courseType;
    }

    public String getCourseCode()     { return courseCode; }
    public String getCourseName()     { return courseName; }
    public int getCredits()           { return credits; }
    public CourseType getCourseType() { return courseType; }
    public List<Student> getStudents(){ return students; }
    public List<Teacher> getTeachers(){ return teachers; }

    public void addStudent(Student s)    { if (!students.contains(s)) students.add(s); }
    public void assignTeacher(Teacher t) { teachers.add(t); }

    @Override public String toString() {
        return courseCode + " - " + courseName + " (" + credits + " cr | " + courseType + ")";
    }

    @Override public boolean equals(Object obj) {
        if (!(obj instanceof Course)) return false;
        return courseCode.equals(((Course) obj).courseCode);
    }

    @Override public int hashCode() { return Objects.hash(courseCode); }
}
