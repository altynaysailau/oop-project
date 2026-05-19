package service;

import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;
import exceptions.MaxFailsExceededException;
import model.Course;
import model.Mark;
import model.Student;
import model.Teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AcademicService {
    private List<Course> courses;
    private List<Student> students;

    public AcademicService() {
        this.courses  = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addCourse(Course course)    { courses.add(course); }
    public void addStudent(Student student) { students.add(student); }
    public List<Course> getCourses()        { return courses; }
    public List<Student> getStudents()      { return students; }

    public void registerStudentToCourse(Student student, Course course) {
        try {
            student.registerForCourse(course);
            System.out.println("Registration successful: " + student.getName()
                    + " -> " + course.getCourseName());
        } catch (CreditLimitException | CourseAlreadyRegisteredException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public void putMark(Student student, Course course, Mark mark) {
        if (!student.getCourses().contains(course)) {
            System.out.println("Cannot put mark. Student not registered for this course.");
            return;
        }
        try {
            student.addMark(course, mark);
            System.out.println("Mark added for " + student.getName());
        } catch (MaxFailsExceededException e) {
            System.out.println("❌  " + e.getMessage());
        }
    }

    public void viewAllCourses() {
        System.out.println("All courses:");
        for (Course course : courses) System.out.println("  " + course);
    }

    public void viewAllStudents() {
        System.out.println("All students:");
        for (Student student : students) System.out.println("  " + student);
    }

    public void viewStudentsSortedByName() {
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted);
        System.out.println("Students sorted by name:");
        for (Student s : sorted) System.out.println("  " + s);
    }

    public void viewCoursesSortedByCredits() {
        List<Course> sorted = new ArrayList<>(courses);
        sorted.sort(Comparator.comparingInt(Course::getCredits));
        System.out.println("Courses sorted by credits:");
        for (Course c : sorted) System.out.println("  " + c);
    }

    /** Generate a simple academic performance report */
    public void generateReport(List<Student> students) {
        System.out.println("=== Academic Performance Report ===");
        System.out.println("Total students: " + students.size());
        for (Student s : students) {
            System.out.println("  " + s.getName()
                + " | credits=" + s.getTotalCredits()
                + " | fails=" + s.getFailCount());
        }
    }
}