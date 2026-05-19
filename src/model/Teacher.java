package model;

import enums.TeacherType;
import enums.Urgency;
import exceptions.MaxFailsExceededException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String teacherId;
    private TeacherType teacherType;
    private List<Course> courses;

    public Teacher(String firstName, String employeeId, String teacherId, TeacherType teacherType) {
        super(firstName, employeeId, "Teacher");
        this.teacherId = teacherId;
        this.teacherType = teacherType;
        this.courses = new ArrayList<>();
    }

    public String getTeacherId()        { return teacherId; }
    public TeacherType getTeacherType() { return teacherType; }
    public void addCourse(Course c)     { courses.add(c); }
    public List<Course> getCourses()    { return courses; }

    /**
     * Assign a mark to a student for a course.
     */
    public void assignMark(Student student, Course course, Mark mark)
            throws MaxFailsExceededException {
        student.addMark(course, mark);
        System.out.println(getFirstName() + " assigned mark ["
            + mark.getType() + ": " + mark.getScore() + "] to " + student.getName());
    }

    /**
     * Send a complaint about a student to the dean with an urgency level.
     */
    public void sendComplaint(Student student, String complaint, Urgency urgency) {
        System.out.println("📢  [COMPLAINT | " + urgency + "] "
            + getFirstName() + " -> Dean");
        System.out.println("    Student : " + student.getName());
        System.out.println("    Reason  : " + complaint);
    }

    /** Overload without urgency for backward compatibility */
    public void sendComplaint(Student student, String complaint) {
        sendComplaint(student, complaint, Urgency.MEDIUM);
    }

    public void viewStudents() {
        for (Course c : courses) {
            System.out.println("Course: " + c.getCourseName());
            for (Student s : c.getStudents()) System.out.println("  " + s);
        }
    }

    /** Teachers who are Professors are always Researchers */
    public boolean isAlwaysResearcher() {
        return teacherType == TeacherType.PROFESSOR;
    }

    @Override
    public String toString() {
        return "Teacher{" + getFirstName() + " | " + teacherId + " | " + teacherType + "}";
    }
}