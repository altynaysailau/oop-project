package model;

import enums.ManagerType;

import java.io.Serializable;
import java.util.*;

public class Manager extends Employee implements Serializable {
    private int managerId;
    private ManagerType managerType;
    private List<Course> coursesForRegistration;

    public Manager(int userId, String firstName, String lastName,
                   String password, String email, int employeeId,
                   int managerId, ManagerType managerType) {
        super(userId, firstName, lastName, password, email, employeeId, "Manager");
        this.managerId = managerId;
        this.managerType = managerType;
        this.coursesForRegistration = new ArrayList<>();
    }

    public int getManagerId() { return managerId; }
    public ManagerType getManagerType() { return managerType; }
    public void setManagerType(ManagerType managerType) { this.managerType = managerType; }

    /**
     * Assign a course to a teacher
     */
    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.addCourse(course);
        course.assignTeacher(teacher);
        System.out.println(getFirstName() + " assigned course [" + course.getCourseName()
                + "] to teacher " + teacher.getFirstName() + " " + teacher.getLastName());
    }

    /**
     * Approve a student's course registration
     */
    public void approveRegistration(Student student, Course course) {
        System.out.println(getFirstName() + " approved registration of "
                + student.getName() + " for course: " + course.getCourseName());
    }

    /**
     * Add a course to the registration pool
     * @param course course to add
     * @param major  intended major (e.g. "CS", "SE")
     * @param year   intended year of study
     */
    public void addCourseForRegistration(Course course, String major, int year) {
        coursesForRegistration.add(course);
        System.out.println("Course [" + course.getCourseName() + "] added for registration "
                + "(Major: " + major + ", Year: " + year + ")");
    }

    /**
     * Create a statistical report about academic performance
     */
    public void createStatisticalReport(List<Student> students) {
        System.out.println("=== Statistical Report by " + getFirstName() + " ===");
        System.out.println("Total students  : " + students.size());

        int totalCredits = 0;
        for (Student s : students) {
            totalCredits += s.getTotalCredits();
        }
        double avgCredits = students.isEmpty() ? 0 : (double) totalCredits / students.size();
        System.out.printf("Avg credits/student: %.1f%n", avgCredits);

        System.out.println("Student list:");
        for (Student s : students) {
            System.out.println("  " + s);
        }
    }

    /**
     * View students sorted alphabetically
     */
    public void viewStudentsAlphabetically(List<Student> students) {
        System.out.println("=== Students sorted alphabetically ===");
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted); // uses Student.compareTo
        for (Student s : sorted) {
            System.out.println(s);
        }
    }

    /**
     * View teachers sorted alphabetically
     */
    public void viewTeachersAlphabetically(List<Teacher> teachers) {
        System.out.println("=== Teachers sorted alphabetically ===");
        List<Teacher> sorted = new ArrayList<>(teachers);
        sorted.sort(Comparator.comparing(t -> t.getFirstName() + t.getLastName()));
        for (Teacher t : sorted) {
            System.out.println(t);
        }
    }

    /**
     * View all students and teachers
     */
    public void viewStudentsAndTeachers(List<Student> students, List<Teacher> teachers) {
        System.out.println("=== Students ===");
        for (Student s : students) System.out.println(s);
        System.out.println("=== Teachers ===");
        for (Teacher t : teachers) System.out.println(t);
    }

    public List<Course> getCoursesForRegistration() { return coursesForRegistration; }

    @Override
    public String toString() {
        return "Manager{id=" + managerId + ", name=" + getFirstName() + " " + getLastName()
                + ", type=" + managerType + "}";
    }
}
