package model;

import enums.TeacherType;
import enums.Urgency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Employee implements Serializable {
    private String teacherId;
    private TeacherType teacherType;
    private List<Course> courses;

   
    public Teacher(String firstName, String employeeId, String teacherId, TeacherType teacherType) {
        super(firstName, employeeId, "Teacher");
        this.teacherId = teacherId;
        this.teacherType = teacherType;
        this.courses = new ArrayList<>();
    }

    public String getTeacherId()          { return teacherId; }
    public TeacherType getTeacherType()   { return teacherType; }

    public void addCourse(Course c)       { courses.add(c); }
    public List<Course> getCourses()      { return courses; }

    public void assignMark(Student student, Course course, Mark mark) {
        student.addMark(course, mark);
        System.out.println(getFirstName() + " assigned mark to " + student.getName());
    }

    public void sendComplaint(Student student, String complaint) {
        System.out.println("Complaint about " + student.getName() + ": " + complaint);
    }

    public void viewStudents() {
        for (Course c : courses) {
            for (Student s : c.getStudents()) System.out.println("  " + s);
        }
    }

    @Override
    public String toString() {
        return "Teacher{" + getFirstName() + " | " + teacherId + " | " + teacherType + "}";
    }
}

