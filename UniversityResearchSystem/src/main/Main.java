package main;

import enums.*;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("  University Research System — Demo");
        System.out.println("========================================\n");

        // 1. Create Users 
        // Admin(int userId, String firstName, String lastName, String password, String email, int employeeId, int adminId)
        Admin admin = new Admin(1, "Alice", "Ivanova", "admin123", "alice@uni.edu", 101, 1001);

        // Manager(int userId, String firstName, String lastName, String password, String email, int employeeId, int managerId, ManagerType)
        Manager manager = new Manager(2, "Bob", "Seitkali", "mgr123", "bob@uni.edu",
                102, 2001, ManagerType.DEAN);

        // TechSupporter(int userId, String firstName, String lastName, String password, String email, int employeeId, int supportId)
        TechSupporter techSupporter = new TechSupporter(3, "Charlie", "Lee", "tech123",
                "charlie@uni.edu", 103, 3001);

        // Teacher(String firstName, String employeeId, String teacherId, TeacherType)  — Person 1's constructor
        Teacher teacher = new Teacher("Diana", "E104", "T4001", TeacherType.PROFESSOR);

        // Student(String id, String name, String major, int yearOfStudy)  — Person 1's constructor
        Student student1 = new Student("SE2301", "Eren Yeager", "Software Engineering", 2);
        Student student2 = new Student("CS2201", "Mikasa Ackerman", "Computer Science", 2);

        // Course(String courseCode, String courseName, int credits, CourseType)  — Person 1's constructor
        Course course1 = new Course("CS301", "Object-Oriented Programming", 6, CourseType.MAJOR);
        Course course2 = new Course("CS302", "Algorithms", 5, CourseType.MAJOR);

        //2. Admin: manage users
        System.out.println("--- ADMIN: Manage Users ---");
        admin.login("admin123");
        // admin manages Employee-based users (Teacher, Manager, TechSupporter extend Employee → User)
        admin.addUser(teacher);
        admin.addUser(techSupporter);
        admin.viewUsers();
        System.out.println();

        admin.updateUser(teacher, "Diana", "Kim", "diana.kim@uni.edu");
        admin.removeUser(techSupporter);
        admin.viewUsers();
        System.out.println();

        //3. Admin: view logs 
        System.out.println("--- ADMIN: View Logs ---");
        admin.viewLogs();
        System.out.println();

        //4. Manager: assign course to teacher 
        System.out.println("--- MANAGER: Assign Course to Teacher ---");
        manager.login("mgr123");
        manager.assignCourseToTeacher(course1, teacher);
        manager.assignCourseToTeacher(course2, teacher);
        System.out.println();

        // ─── 5. Manager: add courses for registration ─────────────────
        System.out.println("--- MANAGER: Add Courses for Registration ---");
        manager.addCourseForRegistration(course1, "Software Engineering", 2);
        manager.addCourseForRegistration(course2, "Computer Science", 2);
        System.out.println();

        // 6. Manager: approve registration
        System.out.println("--- MANAGER: Approve Registration ---");
        manager.approveRegistration(student1, course1);
        System.out.println();

        //7. Manager: statistical report
        System.out.println("--- MANAGER: Statistical Report ---");
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        manager.createStatisticalReport(studentList);
        System.out.println();

        System.out.println("--- MANAGER: View Students Alphabetically ---");
        manager.viewStudentsAlphabetically(studentList);
        System.out.println();

        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);
        System.out.println("--- MANAGER: View Teachers ---");
        manager.viewTeachersAlphabetically(teacherList);
        System.out.println();

        //8. Tech Support workflow
        System.out.println("--- TECH SUPPORT: Request Workflow ---");
        techSupporter.login("tech123");

        TechRequest req1 = new TechRequest(1, "Projector broken in room 305", Urgency.HIGH, admin);
        TechRequest req2 = new TechRequest(2, "Printer out of paper in library", Urgency.LOW, admin);
        TechRequest req3 = new TechRequest(3, "WiFi not working in Lab 2", Urgency.MEDIUM, manager);

        List<TechRequest> allRequests = new ArrayList<>();
        allRequests.add(req1);
        allRequests.add(req2);
        allRequests.add(req3);

        techSupporter.viewNewRequests(allRequests);
        System.out.println();

        // View -> VIEWED status
        techSupporter.viewRequest(req1);
        System.out.println();

        // Accept req1
        techSupporter.acceptRequest(req1);
        // Complete req1
        techSupporter.completeRequest(req1);
        System.out.println();

        // Reject req2
        techSupporter.acceptRequest(req2);
        techSupporter.rejectRequest(req2);
        System.out.println();

        // Accept and complete req3
        techSupporter.acceptRequest(req3);
        techSupporter.completeRequest(req3);
        System.out.println();

        // 9. Send messages between employees
        System.out.println("--- EMPLOYEES: Send Messages ---");
        manager.sendMessageToEmployee(teacher, "Please upload grades by Friday.");
        teacher.viewMessages();
        System.out.println();

        admin.sendMessageToEmployee(techSupporter, "Please fix the server room AC.");
        techSupporter.viewMessages();
        System.out.println();

        // 10. Logout
        admin.logout();
        manager.logout();
        techSupporter.logout();

        System.out.println("\n========================================");
        System.out.println("  Demo completed successfully!");
        System.out.println("========================================");
    }
}
