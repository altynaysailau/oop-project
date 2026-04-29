package service;

import model.Course;
import model.Mark;
import model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exceptions.CourseAlreadyRegisteredException;
import exceptions.CreditLimitException;

public class AcademicService {
    private List<Course> courses;
    private List<Student> students;

    public AcademicService() {
        this.courses = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

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
            System.out.println("Cannot put mark. Student is not registered for this course.");
            return;
        }

        student.addMark(course, mark);
        System.out.println("Mark added for " + student.getName());
    }

    public void viewAllCourses() {
        System.out.println("All courses:");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    public void viewAllStudents() {
        System.out.println("All students:");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void viewStudentsSortedByName() {
        Collections.sort(students);

        System.out.println("Students sorted by name:");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void viewCoursesSortedByCredits() {
        courses.sort(Comparator.comparingInt(Course::getCredits));

        System.out.println("Courses sorted by credits:");
        for (Course course : courses) {
            System.out.println(course);
        }
    }
}