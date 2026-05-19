package main;

import enums.*;
import exceptions.*;
import model.*;
import service.*;

import java.io.*;
import java.util.*;

public class Main {

    // ── Language support ──────────────────────────────────────────────────────
    enum Lang { EN, RU, KZ }
    static Lang lang = Lang.EN;
    static String t(String en, String ru, String kz) {
        return switch (lang) { case RU -> ru; case KZ -> kz; default -> en; };
    }

    // ── Student Organisation (inner class) ────────────────────────────────────
    static class StudentOrganization implements Serializable {
        private static final long serialVersionUID = 1L;
        String name;
        Student head;
        List<Student> members = new ArrayList<>();
        StudentOrganization(String name, Student head) {
            this.name = name; this.head = head; members.add(head);
        }
        void addMember(Student s) {
            if (members.contains(s)) { System.out.println(t("Already a member.","Уже участник.","Мүше болып тіркелген.")); return; }
            members.add(s);
            System.out.println("✅  " + s.getName() + " " + t("joined ","вступил в ","қосылды: ") + name);
        }
        void removeMember(Student s) {
            if (s.equals(head)) { System.out.println(t("Cannot remove head.","Нельзя удалить главу.","Жетекшіні жою мүмкін емес.")); return; }
            members.remove(s);
            System.out.println(s.getName() + " " + t("left ","вышел из ","шықты: ") + name);
        }
        @Override public String toString() {
            return "Org{" + name + ", head=" + head.getName() + ", members=" + members.size() + "}";
        }
    }

    // ── Teacher ratings ───────────────────────────────────────────────────────
    static Map<Teacher, List<Integer>> teacherRatings = new HashMap<>();
    static void rateTeacher(Teacher teacher, int rating) {
        if (rating < 1 || rating > 5) { System.out.println(t("Rating must be 1-5.","Оценка должна быть 1-5.","Баға 1-5 аралығында болуы керек.")); return; }
        teacherRatings.computeIfAbsent(teacher, k -> new ArrayList<>()).add(rating);
        System.out.println("✅  " + t("Rated ","Оценили ","Бағаланды: ") + teacher.getFirstName() + ": " + rating + "/5");
    }
    static double getAvgRating(Teacher teacher) {
        List<Integer> r = teacherRatings.get(teacher);
        if (r == null || r.isEmpty()) return 0.0;
        return r.stream().mapToInt(i -> i).average().orElse(0.0);
    }

    // ── Serialisation ─────────────────────────────────────────────────────────
    static final String SAVE_FILE = "university_data.ser";
    static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L;
        List<Student> students; List<TechRequest> requests;
        List<News> news; List<ResearchPaper> papers;
        List<StudentOrganization> orgs; List<Journal> journals;
        Map<String, List<Integer>> ratings;
        SystemState(List<Student> s, List<TechRequest> rq, List<News> n,
                    List<ResearchPaper> p, List<StudentOrganization> o,
                    List<Journal> j, Map<Teacher,List<Integer>> rm) {
            students=new ArrayList<>(s); requests=new ArrayList<>(rq);
            news=new ArrayList<>(n); papers=new ArrayList<>(p);
            orgs=new ArrayList<>(o); journals=new ArrayList<>(j);
            ratings=new HashMap<>();
            for (var e:rm.entrySet()) ratings.put(e.getKey().getFirstName(), new ArrayList<>(e.getValue()));
        }
    }
    static void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(new SystemState(allStudents,allRequests,allNews,allPapers,allOrgs,allJournals,teacherRatings));
            System.out.println("💾  " + t("Saved to ","Сохранено в ","Сақталды: ") + SAVE_FILE);
        } catch (IOException e) { System.out.println(t("Save failed: ","Ошибка: ","Қате: ")+e.getMessage()); }
    }
    @SuppressWarnings("unchecked")
    static void loadState() {
        if (!new File(SAVE_FILE).exists()) { System.out.println(t("No save file.","Файл не найден.","Файл табылмады.")); return; }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            SystemState st = (SystemState) ois.readObject();
            allStudents.clear(); allStudents.addAll(st.students);
            allRequests.clear(); allRequests.addAll(st.requests);
            allNews.clear();     allNews.addAll(st.news);
            allPapers.clear();   allPapers.addAll(st.papers);
            allOrgs.clear();     allOrgs.addAll(st.orgs);
            allJournals.clear(); allJournals.addAll(st.journals);
            teacherRatings.clear();
            for (Teacher teacher : allTeachers) {
                List<Integer> r = st.ratings.get(teacher.getFirstName());
                if (r != null) teacherRatings.put(teacher, r);
            }
            System.out.println("📂  " + t("Loaded.","Загружено.","Жүктелді."));
        } catch (IOException | ClassNotFoundException e) { System.out.println(t("Load failed: ","Ошибка: ","Қате: ")+e.getMessage()); }
    }

    // ── Shared state ──────────────────────────────────────────────────────────
    static AcademicService academicService = new AcademicService();
    static List<Student>             allStudents    = new ArrayList<>();
    static List<Teacher>             allTeachers    = new ArrayList<>();
    static List<TechRequest>         allRequests    = new ArrayList<>();
    static List<News>                allNews        = new ArrayList<>();
    static List<ResearchPaper>       allPapers      = new ArrayList<>();
    static List<Researcher>          allResearchers = new ArrayList<>();
    static List<StudentOrganization> allOrgs        = new ArrayList<>();
    static List<Journal>             allJournals    = new ArrayList<>();

    static Student         s1, s2;
    static GraduateStudent gs1;
    static Teacher         t1, t2;
    static Manager         mgr;
    static Admin           admin;
    static Researcher      r1, r2;
    static TechSupporter   ts;
    static Course          c1, c2, c3;
    static ResearchProject project1;
    static Scanner         scanner = new Scanner(System.in);

    // ── Bootstrap ─────────────────────────────────────────────────────────────
    static void initData() {
        // Courses
        c1 = new Course("CS101",   "Intro to Programming", 5, CourseType.MAJOR);
        c2 = new Course("MATH201", "Calculus II",          4, CourseType.MINOR);
        c3 = new Course("PE101",   "Physical Education",   2, CourseType.FREE_ELECTIVE);
        academicService.addCourse(c1);
        academicService.addCourse(c2);
        academicService.addCourse(c3);

        // Students
        s1  = new Student("STU001", "Aibek Nurlanov",  "CS", 2); s1.setPassword("pass1");
        s2  = new Student("STU002", "Zarina Bekova",   "SE", 1); s2.setPassword("pass2");
        gs1 = new GraduateStudent("GRAD001", "Dias Seitkali", "CS", 5, "Machine Learning");
        gs1.setPassword("pass3");
        academicService.addStudent(s1); academicService.addStudent(s2); academicService.addStudent(gs1);
        allStudents.addAll(List.of(s1, s2, gs1));
        try { s1.registerForCourse(c1); s1.registerForCourse(c2); } catch (Exception ignored) {}
        try { s2.registerForCourse(c1); } catch (Exception ignored) {}
        c1.addStudent(s1); c1.addStudent(s2); c2.addStudent(s1);

        // Teachers
        t1 = new Teacher("Assylzhan", "EMP001", "TCH001", TeacherType.SENIOR_LECTOR);
        t2 = new Teacher("Pakizar",   "EMP002", "TCH002", TeacherType.PROFESSOR);
        t1.addCourse(c1); c1.assignTeacher(t1);
        t2.addCourse(c2); c2.assignTeacher(t2);
        allTeachers.addAll(List.of(t1, t2));

        // Manager & Admin
        mgr   = new Manager(10, "Nurlan", "Akhmetov", "pass123", "nurlan@uni.kz", 100, 1, ManagerType.DEAN);
        admin = new Admin(20, "Sara", "Ospanova", "admin123", "sara@uni.kz", 200, 1);

        // Researchers
        r1 = new Researcher(1, "Assylzhan", "Teacher-Researcher");
        r2 = new Researcher(2, "Marat",     "Usenov");

        ResearchPaper p1 = new ResearchPaper(1, "Deep Learning in NLP",        "AI Journal",        12, "10.1/dl");
        ResearchPaper p2 = new ResearchPaper(2, "NLP Survey",                  "AI Journal",        20, "10.1/nlp");
        ResearchPaper p3 = new ResearchPaper(3, "Transformer Models",          "AI Journal",        15, "10.1/tr");
        ResearchPaper p4 = new ResearchPaper(4, "Student Performance Analysis","Education Journal",  8, "10.2/spa");
        p1.addAuthorName("Assylzhan Teacher-Researcher");
        p2.addAuthorName("Assylzhan Teacher-Researcher");
        p3.addAuthorName("Assylzhan Teacher-Researcher");
        p4.addAuthorName("Marat Usenov");
        for (int i=0;i<5;i++) p1.addCite();
        for (int i=0;i<4;i++) p2.addCite();
        for (int i=0;i<3;i++) p3.addCite();
        p4.addCite();
        r1.publishResearchPaper(p1); r1.publishResearchPaper(p2); r1.publishResearchPaper(p3);
        r2.publishResearchPaper(p4);
        allPapers.addAll(List.of(p1,p2,p3,p4));
        allResearchers.addAll(List.of(r1, r2));

        // GraduateStudent supervisor (h-index check)
        gs1.setResearchSupervisor((Object) r1);   // uses safe overload

        project1 = new ResearchProject(1, "AI in Education");
        r1.joinProject(project1);

        // Tech support
        ts = new TechSupporter(30, "Bekzat", "Abilov", "tech123", "bekzat@uni.kz", 300, 1);
        admin.addUser(mgr); admin.addUser(ts);

        TechRequest req1 = new TechRequest(1, "Projector broken in Room 301", Urgency.HIGH, admin);
        TechRequest req2 = new TechRequest(2, "Printer out of ink in Lab 5",  Urgency.LOW,  mgr);
        allRequests.addAll(List.of(req1, req2));

        // News
        News news1 = new News(1, "New Research Paper Published",
                "Assylzhan published on Deep Learning in NLP.", "Research", admin);
        news1.setCategory(NewsCategory.RESEARCH); news1.pin();
        News news2 = new News(2, "Semester Schedule Released",
                "The new semester schedule is now available.", "Academic", admin);
        news2.setCategory(NewsCategory.ACADEMIC);
        allNews.addAll(List.of(news1, news2));

        // Journals (Observer pattern)
        Journal j1 = new Journal(1, "AI Research Quarterly");
        Journal j2 = new Journal(2, "Education & Technology");
        j1.subscribe(admin); j1.subscribe(mgr);
        allJournals.addAll(List.of(j1, j2));

        // Student organisation
        StudentOrganization acm = new StudentOrganization("ACM Student Chapter", s1);
        acm.addMember(s2);
        allOrgs.add(acm);

        System.out.println("\n✅  " + t("Data initialized.","Данные готовы.","Деректер дайын.") + "\n");
    }

    // ── Login helper ──────────────────────────────────────────────────────────
    static boolean passwordPrompt(String correctPassword) {
        System.out.print(t("Password: ","Пароль: ","Құпия сөз: "));
        String entered = scanner.nextLine().trim();
        if (entered.equals(correctPassword)) {
            System.out.println("✅  " + t("Login successful.","Вход выполнен.","Кіру сәтті."));
            return true;
        }
        System.out.println("❌  " + t("Wrong password.","Неверный пароль.","Қате құпия сөз."));
        return false;
    }

    // ── MAIN ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║     UNIVERSITY RESEARCH SYSTEM – Part C      ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Select language / Выберите язык / Тілді таңдаңыз:");
        System.out.println("  1. English  2. Русский  3. Қазақша");
        int lc = readInt("Choice: ");
        lang = switch (lc) { case 2 -> Lang.RU; case 3 -> Lang.KZ; default -> Lang.EN; };
        initData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt(t("Choose: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> {
                    System.out.println("  1. Aibek (pass1)  2. Zarina (pass2)  3. Dias (pass3)[Grad]");
                    int sc = readInt(t("Pick: ","Выбор: ","Таңдаңыз: "));
                    Student chosenS = switch(sc){ case 2->s2; case 3->gs1; default->s1; };
                    if (passwordPrompt(chosenS.getPassword())) studentMenu(chosenS);
                }
                case 2 -> {
                    System.out.println("  1. Assylzhan (teacher1)  2. Pakizar (teacher2)");
                    int tc = readInt(t("Pick: ","Выбор: ","Таңдаңыз: "));
                    Teacher chosenT = (tc==2) ? t2 : t1;
                    if (passwordPrompt(tc==2?"teacher2":"teacher1")) teacherMenu(chosenT);
                }
                case 3 -> { if (passwordPrompt("pass123"))  managerMenu(mgr); }
                case 4 -> { if (passwordPrompt("admin123")) adminMenu(admin); }
                case 5 -> { if (passwordPrompt("research1")) researcherMenu(r1); }
                case 6 -> { if (passwordPrompt("tech123"))  techSupportMenu(ts); }
                case 8 -> switchLanguage();
                case 9 -> saveState();
                case 10-> loadState();
                case 0 -> running = false;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
        System.out.println("\n👋  " + t("Goodbye!","До свидания!","Сау болыңыз!"));
    }

    static void printMainMenu() {
        System.out.println("\n══════════ " + t("MAIN MENU","ГЛАВНОЕ МЕНЮ","БАС МӘЗІР") + " ══════════");
        System.out.println(" 1. " + t("Student","Студент","Студент"));
        System.out.println(" 2. " + t("Teacher","Преподаватель","Оқытушы"));
        System.out.println(" 3. " + t("Manager","Менеджер","Менеджер") + " [pass123]");
        System.out.println(" 4. " + t("Admin","Администратор","Әкімші") + " [admin123]");
        System.out.println(" 5. " + t("Researcher","Исследователь","Зерттеуші") + " [research1]");
        System.out.println(" 6. " + t("TechSupport","Тех.поддержка","Техникалық") + " [tech123]");
        System.out.println(" 7. " + t("Journals (Observer)","Журналы","Журналдар"));
        System.out.println(" 8. " + t("Switch language","Сменить язык","Тілді ауыстыру"));
        System.out.println(" 9. " + t("Save state","Сохранить","Сақтау"));
        System.out.println("10. " + t("Load state","Загрузить","Жүктеу"));
        System.out.println(" 0. " + t("Exit","Выход","Шығу"));
        System.out.println("═".repeat(44));
    }

    static void switchLanguage() {
        System.out.println("1. English  2. Русский  3. Қазақша");
        int lc = readInt("Choice: ");
        lang = switch (lc) { case 2 -> Lang.RU; case 3 -> Lang.KZ; default -> Lang.EN; };
        System.out.println("✅  " + t("Language: English","Язык: Русский","Тіл: Қазақша"));
    }

    // ── Journal / Observer menu ───────────────────────────────────────────────


    // ── Student menu ──────────────────────────────────────────────────────────
    static void studentMenu(Student student) {
        System.out.println("\n🎓  " + t("Student: ","Студент: ","Студент: ") + student.getName());
        boolean back = false;
        while (!back) {
            System.out.println("\n─── " + t("Student Menu","Меню студента","Студент мәзірі") + " ───");
            System.out.println(" 1. " + t("View all courses",       "Все курсы",             "Барлық курстар"));
            System.out.println(" 2. " + t("Register for a course",  "Запись на курс",         "Курсқа тіркелу"));
            System.out.println(" 3. " + t("My enrolled courses",    "Мои курсы",              "Менің курстарым"));
            System.out.println(" 4. " + t("View marks",             "Оценки",                 "Бағалар"));
            System.out.println(" 5. " + t("View transcript",        "Транскрипт",             "Транскрипт"));
            System.out.println(" 6. " + t("View teacher of course", "Преподаватель курса",    "Курс оқытушысы"));
            System.out.println(" 7. " + t("Rate a teacher",         "Оценить преподавателя",  "Оқытушыны бағалау"));
            System.out.println(" 8. " + t("Submit tech request",    "Тех. заявка",            "Техникалық сұраныс"));
            System.out.println(" 9. " + t("News feed",              "Новости",                "Жаңалықтар"));
            System.out.println("10. " + t("Comment on news",        "Комментарий",            "Пікір"));
            System.out.println("11. " + t("Student organizations",  "Студ. организации",      "Студ. ұйымдар"));
            System.out.println("12. " + t("Graduate student info",  "Инфо магистранта",       "Магистрант ақпараты"));
            System.out.println(" 0. " + t("Back","Назад","Артқа"));
            System.out.println("─".repeat(44));
            int choice = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> academicService.viewAllCourses();
                case 2 -> {
                    List<Course> avail = List.of(c1,c2,c3);
                    for (int i=0;i<avail.size();i++) System.out.println("  "+(i+1)+". "+avail.get(i));
                    int pick = readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1;
                    if (pick>=0&&pick<avail.size()) academicService.registerStudentToCourse(student,avail.get(pick));
                    else System.out.println(t("Invalid.","Ошибка.","Қате."));
                }
                case 3 -> { if (student.getCourses().isEmpty()) System.out.println("  "+t("(none)","(нет)","(жоқ)")); else student.getCourses().forEach(c->System.out.println("  "+c)); }
                case 4 -> student.viewMarks();
                case 5 -> student.viewTranscript();
                case 6 -> {
                    List<Course> sc = student.getCourses();
                    if (sc.isEmpty()) { System.out.println(t("No courses.","Нет курсов.","Курс жоқ.")); break; }
                    for (int i=0;i<sc.size();i++) System.out.println("  "+(i+1)+". "+sc.get(i).getCourseName());
                    int pick = readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1;
                    if (pick>=0&&pick<sc.size()) {
                        Course chosen = sc.get(pick);
                        if (chosen.getTeachers().isEmpty()) System.out.println(t("  No teacher assigned.","  Не назначен.","  Тағайындалмаған."));
                        else chosen.getTeachers().forEach(tt->System.out.println("  "+tt));
                    }
                }
                case 7 -> {
                    for (int i=0;i<allTeachers.size();i++)
                        System.out.println("  "+(i+1)+". "+allTeachers.get(i).getFirstName()
                                +"  [avg: "+String.format("%.1f",getAvgRating(allTeachers.get(i)))+"/5]");
                    int tp = readInt(t("Pick teacher: ","Преп.: ","Оқытушы: "))-1;
                    if (tp>=0&&tp<allTeachers.size()) {
                        int rating = readInt(t("Rating (1-5): ","Оценка (1-5): ","Баға (1-5): "));
                        rateTeacher(allTeachers.get(tp), rating);
                    }
                }
                case 8 -> {
                    String desc = readString(t("Describe issue: ","Проблема: ","Мәселе: "));
                    System.out.println("1=LOW  2=MEDIUM  3=HIGH");
                    int u = readInt(t("Urgency: ","Срочность: ","Шұғылдық: "));
                    Urgency urg = switch(u){ case 2->Urgency.MEDIUM; case 3->Urgency.HIGH; default->Urgency.LOW; };
                    allRequests.add(new TechRequest(allRequests.size()+1, desc, urg, admin));
                    System.out.println("✅  " + t("Request submitted.","Заявка отправлена.","Сұраныс жіберілді."));
                }
                case 9 -> {
                    System.out.println("\n═══ " + t("News (pinned first)","Новости","Жаңалықтар") + " ═══");
                    allNews.stream().sorted((a,b)->Boolean.compare(b.isPinned(),a.isPinned())).forEach(News::viewNews);
                }
                case 10 -> {
                    for (int i=0;i<allNews.size();i++) System.out.println("  "+(i+1)+". "+allNews.get(i).getTitle());
                    int pick = readInt(t("Pick news: ","Новость: ","Жаңалық: "))-1;
                    if (pick>=0&&pick<allNews.size()) {
                        String txt = readString(t("Comment: ","Комментарий: ","Пікір: "));
                        allNews.get(pick).addComment(new Comment(txt, admin), admin);
                    }
                }
                case 11 -> studentOrgsMenu(student);
                case 12 -> {
                    if (student instanceof GraduateStudent grad) {
                        System.out.println(t("Topic: ","Тема: ","Тақырып: ")+grad.getResearchTopic());
                        System.out.println(t("Supervisor: ","Руководитель: ","Жетекші: ")+grad.getResearchSupervisor());
                        System.out.println(t("Diploma papers: ","Дипломных работ: ","Диплом жұмыстары: ")+grad.getDiplomaPapers().size());
                    } else System.out.println(t("Not a graduate student.","Не магистрант.","Магистрант емес."));
                }
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
    }

    static void studentOrgsMenu(Student student) {
        boolean back = false;
        while (!back) {
            System.out.println("\n─── "+t("Organizations","Организации","Ұйымдар")+" ───");
            System.out.println(" 1. "+t("View all","Все","Барлығы"));
            System.out.println(" 2. "+t("Join","Вступить","Қосылу"));
            System.out.println(" 3. "+t("Leave","Выйти","Шығу"));
            System.out.println(" 4. "+t("Create new","Создать","Жаңа"));
            System.out.println(" 0. "+t("Back","Назад","Артқа"));
            int ch = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (ch) {
                case 1 -> { if(allOrgs.isEmpty()){System.out.println(t("None.","Нет.","Жоқ."));break;} allOrgs.forEach(o->{System.out.println("\n  "+o);o.members.forEach(m->System.out.println("    - "+m.getName()+(m.equals(o.head)?" 👑":"")));});}
                case 2 -> { if(allOrgs.isEmpty()){System.out.println(t("No orgs.","Нет.","Жоқ."));break;} for(int i=0;i<allOrgs.size();i++)System.out.println("  "+(i+1)+". "+allOrgs.get(i).name); int pick=readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1; if(pick>=0&&pick<allOrgs.size())allOrgs.get(pick).addMember(student); }
                case 3 -> { List<StudentOrganization> mine=allOrgs.stream().filter(o->o.members.contains(student)).toList(); if(mine.isEmpty()){System.out.println(t("Not in any.","Не в орг.","Ұйымда жоқ."));break;} for(int i=0;i<mine.size();i++)System.out.println("  "+(i+1)+". "+mine.get(i).name); int pick=readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1; if(pick>=0&&pick<mine.size())mine.get(pick).removeMember(student); }
                case 4 -> { String n=readString(t("Name: ","Название: ","Атауы: ")); allOrgs.add(new StudentOrganization(n,student)); System.out.println("✅  "+t("Created. You are HEAD.","Создано. Вы ГЛАВА.","Құрылды. Сіз ЖЕТЕКШІ.")); }
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Ошибка.","Қате."));
            }
        }
    }

    // ── Teacher menu ──────────────────────────────────────────────────────────
    static void teacherMenu(Teacher teacher) {
        Researcher linkedR = allResearchers.stream()
                .filter(r -> r.getFirstName().equals(teacher.getFirstName()))
                .findFirst().orElse(null);
        boolean isResearcher = linkedR != null;
        System.out.println("\n📚  "+t("Teacher: ","Преподаватель: ","Оқытушы: ")+teacher.getFirstName()+" ("+teacher.getTeacherType()+")");
        if (isResearcher) System.out.println("🔬  "+t("Also a Researcher (option 9)","Также исследователь (пункт 9)","Зерттеуші де (9)"));

        boolean back = false;
        while (!back) {
            System.out.println("\n─── "+t("Teacher Menu","Меню преподавателя","Оқытушы мәзірі")+" ───");
            System.out.println(" 1. "+t("My courses",        "Мои курсы",           "Менің курстарым"));
            System.out.println(" 2. "+t("View students",      "Студенты",            "Студенттер"));
            System.out.println(" 3. "+t("Put a mark",         "Выставить оценку",    "Баға қою"));
            System.out.println(" 4. "+t("Send complaint",     "Отправить жалобу",    "Шағым жіберу"));
            System.out.println(" 5. "+t("Add lesson",         "Добавить занятие",    "Сабақ қосу"));
            System.out.println(" 6. "+t("Send message",       "Сообщение менеджеру", "Менеджерге хабар"));
            System.out.println(" 7. "+t("View news",          "Новости",             "Жаңалықтар"));
            System.out.println(" 8. "+t("My ratings",         "Мой рейтинг",         "Менің рейтингім"));
            if (isResearcher)
            System.out.println(" 9. "+t("Research menu",      "Меню исследователя",  "Зерттеуші мәзірі"));
            System.out.println(" 0. "+t("Back","Назад","Артқа"));
            System.out.println("─".repeat(44));
            int choice = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> { if(teacher.getCourses().isEmpty())System.out.println("  "+t("(none)","(нет)","(жоқ)")); else teacher.getCourses().forEach(c->System.out.println("  "+c)); }
                case 2 -> teacher.viewStudents();
                case 3 -> {
                    List<Course> tc = teacher.getCourses();
                    if(tc.isEmpty()){System.out.println(t("No courses.","Нет курсов.","Курс жоқ."));break;}
                    for(int i=0;i<tc.size();i++) System.out.println("  "+(i+1)+". "+tc.get(i).getCourseName());
                    int cp=readInt(t("Pick course: ","Курс: ","Курс: "))-1;
                    if(cp<0||cp>=tc.size()){System.out.println(t("Invalid.","Ошибка.","Қате."));break;}
                    List<Student> cs=tc.get(cp).getStudents();
                    if(cs.isEmpty()){System.out.println(t("No students.","Нет студентов.","Студент жоқ."));break;}
                    for(int i=0;i<cs.size();i++) System.out.println("  "+(i+1)+". "+cs.get(i).getName());
                    int sp=readInt(t("Pick student: ","Студент: ","Студент: "))-1;
                    if(sp<0||sp>=cs.size()){System.out.println(t("Invalid.","Ошибка.","Қате."));break;}
                    System.out.println("1=FIRST_ATTESTATION  2=SECOND_ATTESTATION  3=FINAL");
                    int mt=readInt(t("Mark type: ","Тип: ","Түрі: "));
                    MarkType markType=switch(mt){case 2->MarkType.SECOND_ATTESTATION;case 3->MarkType.FINAL;default->MarkType.FIRST_ATTESTATION;};
                    double score=readDouble(t("Score (0-100): ","Балл (0-100): ","Балл (0-100): "));
                    try{teacher.assignMark(cs.get(sp),tc.get(cp),new Mark(markType,score));}
                    catch(MaxFailsExceededException e){System.out.println("❌  "+e.getMessage());}
                }
                case 4 -> {
                    for(int i=0;i<allStudents.size();i++) System.out.println("  "+(i+1)+". "+allStudents.get(i).getName());
                    int sp=readInt(t("Pick student: ","Студент: ","Студент: "))-1;
                    if(sp<0||sp>=allStudents.size()){System.out.println(t("Invalid.","Ошибка.","Қате."));break;}
                    String complaint=readString(t("Complaint: ","Жалоба: ","Шағым: "));
                    System.out.println("1=LOW  2=MEDIUM  3=HIGH");
                    int u=readInt(t("Urgency: ","Срочность: ","Шұғылдық: "));
                    Urgency urg=switch(u){case 2->Urgency.MEDIUM;case 3->Urgency.HIGH;default->Urgency.LOW;};
                    teacher.sendComplaint(allStudents.get(sp), complaint, urg);
                }
                case 5 -> {
                    System.out.println("1=LECTURE  2=PRACTICE");
                    LessonType lt=(readInt(t("Type: ","Тип: ","Түрі: "))==2)?LessonType.PRACTICE:LessonType.LECTURE;
                    String topic=readString(t("Topic: ","Тема: ","Тақырып: "));
                    String day=readString(t("Day: ","День: ","Күн: "));
                    String time=readString(t("Time: ","Время: ","Уақыт: "));
                    String room=readString(t("Room: ","Аудитория: ","Аудитория: "));
                    System.out.println("✅  "+new Lesson(topic,lt,day,time,room));
                }
                case 6 -> {
                    String content=readString(t("Message: ","Сообщение: ","Хабар: "));
                    Message msg=new Message(admin,mgr,content);
                    admin.sendMessage(msg); mgr.receiveMessage(msg);
                    System.out.println("✅  "+t("Sent to Manager.","Отправлено менеджеру.","Менеджерге жіберілді."));
                }
                case 7 -> allNews.stream().sorted((a,b)->Boolean.compare(b.isPinned(),a.isPinned())).forEach(n->System.out.println("["+(n.isPinned()?"📌":"  ")+"] "+n.getTitle()));
                case 8 -> {
                    double avg=getAvgRating(teacher);
                    List<Integer> rats=teacherRatings.getOrDefault(teacher,List.of());
                    System.out.println(t("Average rating: ","Средний рейтинг: ","Орташа рейтинг: ")+String.format("%.1f",avg)+"/5  ("+rats.size()+" "+t("votes","голосов","дауыс")+")");
                }
                case 9 -> { if(isResearcher) researcherMenu(linkedR); else System.out.println(t("Not a researcher.","Не исследователь.","Зерттеуші емес.")); }
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
    }

    // ── Manager menu ──────────────────────────────────────────────────────────
    static void managerMenu(Manager manager) {
        System.out.println("\n🏛️  "+t("Manager: ","Менеджер: ","Менеджер: ")+manager.getFirstName()+" ("+manager.getManagerType()+")");
        boolean back = false;
        while (!back) {
            System.out.println("\n─── "+t("Manager Menu","Меню менеджера","Менеджер мәзірі")+" ───");
            System.out.println(" 1. "+t("Assign course to teacher",   "Назначить курс",        "Курс тағайындау"));
            System.out.println(" 2. "+t("Approve registration",       "Подтвердить",           "Тіркеуді растау"));
            System.out.println(" 3. "+t("Add course for registration","Добавить курс",          "Курс қосу"));
            System.out.println(" 4. "+t("Statistical report",         "Статистика",             "Статистика"));
            System.out.println(" 5. "+t("Students A-Z",               "Студенты А-Я",           "Студенттер А-Я"));
            System.out.println(" 6. "+t("Teachers A-Z",               "Преподаватели А-Я",      "Оқытушылар А-Я"));
            System.out.println(" 7. "+t("All students & teachers",    "Все",                   "Барлығы"));
            System.out.println(" 8. "+t("Manage news",                "Новости",               "Жаңалықтар"));
            System.out.println(" 9. "+t("Students by credits",        "По кредитам",            "Кредит бойынша"));
            System.out.println(" 0. "+t("Back","Назад","Артқа"));
            System.out.println("─".repeat(44));
            int choice = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> {
                    System.out.println("1="+c1.getCourseName()+"  2="+c2.getCourseName()+"  3="+c3.getCourseName());
                    int cp=readInt(t("Course: ","Курс: ","Курс: "));
                    Course picked=switch(cp){case 2->c2;case 3->c3;default->c1;};
                    System.out.println("1="+t1.getFirstName()+"  2="+t2.getFirstName());
                    int tp=readInt(t("Teacher: ","Преп.: ","Оқытушы: "));
                    manager.assignCourseToTeacher(picked,tp==2?t2:t1);
                }
                case 2 -> {
                    for(int i=0;i<allStudents.size();i++) System.out.println("  "+(i+1)+". "+allStudents.get(i).getName());
                    int sp=readInt(t("Student: ","Студент: ","Студент: "))-1;
                    System.out.println("1="+c1.getCourseName()+"  2="+c2.getCourseName()+"  3="+c3.getCourseName());
                    int cp=readInt(t("Course: ","Курс: ","Курс: "));
                    Course picked=switch(cp){case 2->c2;case 3->c3;default->c1;};
                    if(sp>=0&&sp<allStudents.size()) manager.approveRegistration(allStudents.get(sp),picked);
                }
                case 3 -> {
                    String code=readString(t("Code: ","Код: ","Код: "));
                    String cname=readString(t("Name: ","Название: ","Атауы: "));
                    int cred=readInt(t("Credits: ","Кредиты: ","Кредиттер: "));
                    System.out.println("1=MAJOR  2=MINOR  3=FREE_ELECTIVE");
                    int ct=readInt(t("Type: ","Тип: ","Түрі: "));
                    CourseType ctype=switch(ct){case 2->CourseType.MINOR;case 3->CourseType.FREE_ELECTIVE;default->CourseType.MAJOR;};
                    Course nc=new Course(code,cname,cred,ctype);
                    academicService.addCourse(nc);
                    String major=readString(t("Major: ","Специальность: ","Мамандық: "));
                    int year=readInt(t("Year: ","Курс: ","Жыл: "));
                    manager.addCourseForRegistration(nc,major,year);
                }
                case 4 -> manager.createStatisticalReport(allStudents);
                case 5 -> manager.viewStudentsAlphabetically(allStudents);
                case 6 -> manager.viewTeachersAlphabetically(allTeachers);
                case 7 -> manager.viewStudentsAndTeachers(allStudents,allTeachers);
                case 8 -> {
                    System.out.println("1="+t("Pin","Закрепить","Бекіту")+"  2="+t("Add news","Добавить","Жаңалық қосу"));
                    int sub=readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
                    if(sub==1){for(int i=0;i<allNews.size();i++)System.out.println("  "+(i+1)+". "+allNews.get(i).getTitle());int np=readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1;if(np>=0&&np<allNews.size())allNews.get(np).pin();}
                    else{String title=readString(t("Title: ","Заголовок: ","Тақырып: "));String content=readString(t("Content: ","Содержание: ","Мазмұн: "));allNews.add(new News(allNews.size()+1,title,content,"General",admin));System.out.println("✅  "+t("News added.","Добавлено.","Қосылды."));}
                }
                case 9 -> {
                    List<Student> byC=new ArrayList<>(allStudents);
                    byC.sort(Comparator.comparingInt(Student::getTotalCredits).reversed());
                    byC.forEach(st->System.out.println("  "+st.getName()+" – "+st.getTotalCredits()+" cr"));
                }
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
    }

    // ── Admin menu ────────────────────────────────────────────────────────────
    static void adminMenu(Admin adminUser) {
        System.out.println("\n🔑  Admin: "+adminUser.getFirstName());
        boolean back = false;
        while (!back) {
            System.out.println("\n─── "+t("Admin Menu","Меню админа","Әкімші мәзірі")+" ───");
            System.out.println(" 1. "+t("View users","Пользователи","Пайдаланушылар"));
            System.out.println(" 2. "+t("Add student","Добавить студента","Студент қосу"));
            System.out.println(" 3. "+t("Remove user","Удалить","Жою"));
            System.out.println(" 4. "+t("Update user","Обновить","Жаңарту"));
            System.out.println(" 5. "+t("View logs","Журнал","Журнал"));
            System.out.println(" 0. "+t("Back","Назад","Артқа"));
            System.out.println("─".repeat(44));
            int choice = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> adminUser.viewUsers();
                case 2 -> {
                    String fn=readString(t("First name: ","Имя: ","Аты: "));
                    String ln=readString(t("Last name: ","Фамилия: ","Тегі: "));
                    String maj=readString(t("Major: ","Специальность: ","Мамандық: "));
                    int yr=readInt(t("Year: ","Курс: ","Жыл: "));
                    Student ns=new Student("STU"+String.format("%03d",allStudents.size()+1),fn+" "+ln,maj,yr);
                    ns.setPassword("newpass");
                    allStudents.add(ns); academicService.addStudent(ns);
                    adminUser.getLogs().add("[ADD] Student: "+fn+" "+ln);
                    System.out.println("✅  "+t("Student added. Default password: newpass","Студент добавлен. Пароль: newpass","Студент қосылды. Пароль: newpass"));
                }
                case 3 -> {
                    List<User> users=adminUser.getUsers();
                    for(int i=0;i<users.size();i++) System.out.println("  "+(i+1)+". "+users.get(i));
                    int up=readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1;
                    if(up>=0&&up<users.size()) adminUser.removeUser(users.get(up));
                }
                case 4 -> {
                    List<User> users=adminUser.getUsers();
                    for(int i=0;i<users.size();i++) System.out.println("  "+(i+1)+". "+users.get(i));
                    int up=readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1;
                    if(up>=0&&up<users.size()){
                        String fn=readString(t("New first name: ","Имя: ","Аты: "));
                        String ln=readString(t("New last name: ","Фамилия: ","Тегі: "));
                        String em=readString(t("New email: ","Email: ","Email: "));
                        adminUser.updateUser(users.get(up),fn,ln,em);
                    }
                }
                case 5 -> adminUser.viewLogs();
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
    }

    // ── Researcher menu ───────────────────────────────────────────────────────
    static void researcherMenu(Researcher researcher) {
        System.out.println("\n🔬  "+t("Researcher: ","Исследователь: ","Зерттеуші: ")+researcher.getFirstName()+" "+researcher.getLastName());
        boolean back = false;
        while (!back) {
            System.out.println("\n─── "+t("Researcher Menu","Меню исследователя","Зерттеуші мәзірі")+" ───");
            System.out.println(" 1. "+t("Calculate h-index",    "Вычислить h-индекс",   "h-индекс есептеу"));
            System.out.println(" 2. "+t("View my papers",       "Мои статьи",           "Менің мақалаларым"));
            System.out.println(" 3. "+t("Print papers sorted",  "Статьи (сортировка)",  "Мақалалар (сұрыптау)"));
            System.out.println(" 4. "+t("Publish new paper",    "Опубликовать статью",  "Мақала жариялау"));
            System.out.println(" 5. "+t("Get citation",         "Получить цитату",      "Дәйексөз"));
            System.out.println(" 6. "+t("Join project",         "Присоединиться",       "Жобаға қосылу"));
            System.out.println(" 7. "+t("My projects",          "Мои проекты",          "Менің жобаларым"));
            System.out.println(" 8. "+t("All researchers",      "Все исследователи",    "Барлық зерттеушілер"));
            System.out.println(" 9. "+t("Top cited",            "Топ по цитатам",       "Ең цитаталанған"));
            System.out.println("10. "+t("Assign supervisor",    "Назначить руководителя","Жетекші тағайындау"));
            System.out.println("11. "+t("Print all papers",     "Все статьи универ.",   "Барлық мақалалар"));
            System.out.println(" 0. "+t("Back","Назад","Артқа"));
            System.out.println("─".repeat(44));
            int choice = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> System.out.println("h-index = "+researcher.calculateHIndex());
                case 2 -> { if(researcher.getPapers().isEmpty())System.out.println("  "+t("(none)","(нет)","(жоқ)")); else researcher.getPapers().forEach(p->System.out.println("  "+p)); }
                case 3 -> {
                    System.out.println("1="+t("Citations","Цитаты","Цитаттар")+"  2="+t("Date","Дата","Күні")+"  3="+t("Pages","Страницы","Беттер"));
                    int s=readInt(t("Sort by: ","Сортировка: ","Сұрыптау: "));
                    Comparator<ResearchPaper> cmp=switch(s){case 2->Comparator.comparing(ResearchPaper::getDatePublished);case 3->Comparator.comparingInt(ResearchPaper::getPages);default->Comparator.comparingInt(ResearchPaper::getCitations).reversed();};
                    researcher.printPapers(cmp);
                }
                case 4 -> {
                    String title=readString(t("Title: ","Название: ","Атауы: "));
                    String journal=readString(t("Journal: ","Журнал: ","Журнал: "));
                    int pages=readInt(t("Pages: ","Страниц: ","Беттер: "));
                    String doi=readString(t("DOI: ","DOI: ","DOI: "));
                    ResearchPaper np=new ResearchPaper(allPapers.size()+1,title,journal,pages,doi);
                    researcher.publishResearchPaper(np);
                    allPapers.add(np);
                    // Auto-pin Research news announcement
                    News ann=new News(allNews.size()+1,"New Paper: "+title,researcher.getFirstName()+" published '"+title+"'.","Research",admin);
                    ann.setCategory(NewsCategory.RESEARCH); ann.pin(); allNews.add(ann);
                    System.out.println("📢  "+t("Pinned announcement created.","Объявление закреплено.","Хабарлама бекітілді."));
                }
                case 5 -> {
                    List<ResearchPaper> mp=researcher.getPapers();
                    if(mp.isEmpty()){System.out.println(t("No papers.","Нет статей.","Мақала жоқ."));break;}
                    for(int i=0;i<mp.size();i++) System.out.println("  "+(i+1)+". "+mp.get(i).getTitle());
                    int pp=readInt(t("Pick: ","Выбор: ","Таңдаңыз: "))-1;
                    if(pp<0||pp>=mp.size()){System.out.println(t("Invalid.","Ошибка.","Қате."));break;}
                    System.out.println("1=PLAIN_TEXT  2=BIBTEX");
                    int fmt=readInt(t("Format: ","Формат: ","Формат: "));
                    CitationFormat cf=(fmt==2)?CitationFormat.BIBTEX:CitationFormat.PLAIN_TEXT;
                    mp.get(pp).getCitation(cf);
                }
                case 6 -> {
                    System.out.println("1. "+project1.getTopic()+"  2. "+t("New project","Новый проект","Жаңа жоба"));
                    if(readInt(t("Choice: ","Выбор: ","Таңдаңыз: "))==1) researcher.joinProject(project1);
                    else{String tpc=readString(t("Topic: ","Тема: ","Тақырып: "));researcher.joinProject(new ResearchProject(allResearchers.size()+100,tpc));}
                }
                case 7 -> { if(researcher.getProjects().isEmpty())System.out.println("  "+t("(none)","(нет)","(жоқ)")); else researcher.getProjects().forEach(p->System.out.println("  "+p)); }
                case 8 -> {
                    System.out.println("1="+t("Citations","Цитаты","Цитаттар")+"  2="+t("Name","Имя","Аты"));
                    int s=readInt(t("Sort: ","Сортировка: ","Сұрыптау: "));
                    List<Researcher> sorted=new ArrayList<>(allResearchers);
                    if(s==2) sorted.sort(Comparator.comparing(Researcher::getLastName));
                    else sorted.sort((a,b)->Integer.compare(b.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum(),a.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum()));
                    sorted.forEach(res->{int total=res.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum();System.out.println("  "+res.getFirstName()+" "+res.getLastName()+" | papers="+res.getPapers().size()+" | citations="+total+" | h="+ResearchService.calculateHIndex(res));});
                }
                case 9 -> {
                    ResearchService.printTopCitedResearcher(allResearchers);
                    allResearchers.stream().max(Comparator.comparingInt(r->r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum())).ifPresent(top->{
                        int total=top.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum();
                        News tn=new News(allNews.size()+1,"Top Researcher: "+top.getFirstName(),top.getFirstName()+" is top cited with "+total+" citations.","Research",admin);
                        tn.setCategory(NewsCategory.RESEARCH);tn.pin();allNews.add(tn);
                        System.out.println("📢  "+t("News generated.","Новость создана.","Жаңалық жасалды."));
                    });
                }
                case 10 -> {
                    for(int i=0;i<allResearchers.size();i++) System.out.println("  "+(i+1)+". "+allResearchers.get(i).getFirstName()+" h="+ResearchService.calculateHIndex(allResearchers.get(i)));
                    int rp=readInt(t("Pick as supervisor: ","Руководитель: ","Жетекші: "))-1;
                    if(rp>=0&&rp<allResearchers.size()){
                        Researcher sup=allResearchers.get(rp);
                        try { gs1.setResearchSupervisor(sup); }
                        catch(LowHIndexException e){ System.out.println("❌  "+e.getMessage()); }
                    }
                }
                case 11 -> {
                    System.out.println("1="+t("Citations","Цитаты","Цитаттар")+"  2="+t("Date","Дата","Күні")+"  3="+t("Pages","Страницы","Беттер"));
                    int s=readInt(t("Sort by: ","Сортировка: ","Сұрыптау: "));
                    Comparator<ResearchPaper> cmp=switch(s){case 2->Comparator.comparing(ResearchPaper::getDatePublished);case 3->Comparator.comparingInt(ResearchPaper::getPages);default->Comparator.comparingInt(ResearchPaper::getCitations).reversed();};
                    ResearchService.printAllPapers(allResearchers,cmp);
                }
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
    }

    // ── Tech support menu ─────────────────────────────────────────────────────
    static void techSupportMenu(TechSupporter supporter) {
        System.out.println("\n🔧  "+t("Tech Support: ","Тех.поддержка: ","Техникалық қолдау: ")+supporter.getFirstName());
        boolean back = false;
        while (!back) {
            System.out.println("\n─── "+t("Tech Support","Тех.поддержка","Техникалық қолдау")+" ───");
            System.out.println(" 1. "+t("View new requests","Новые заявки","Жаңа сұраныстар"));
            System.out.println(" 2. "+t("View request details","Детали","Мәліметтер"));
            System.out.println(" 3. "+t("Accept","Принять","Қабылдау"));
            System.out.println(" 4. "+t("Reject","Отклонить","Қабылдамау"));
            System.out.println(" 5. "+t("Mark DONE","Выполнено","Орындалды"));
            System.out.println(" 6. "+t("All requests","Все заявки","Барлық сұраныстар"));
            System.out.println(" 0. "+t("Back","Назад","Артқа"));
            System.out.println("─".repeat(44));
            int choice = readInt(t("Choice: ","Выбор: ","Таңдаңыз: "));
            switch (choice) {
                case 1 -> supporter.viewNewRequests(allRequests);
                case 2 -> { printReqs(); int r=readInt("# ")-1; if(r>=0&&r<allRequests.size()) supporter.viewRequest(allRequests.get(r)); }
                case 3 -> { printReqs(); int r=readInt("# ")-1; if(r>=0&&r<allRequests.size()) supporter.acceptRequest(allRequests.get(r)); }
                case 4 -> { printReqs(); int r=readInt("# ")-1; if(r>=0&&r<allRequests.size()) supporter.rejectRequest(allRequests.get(r)); }
                case 5 -> { printReqs(); int r=readInt("# ")-1; if(r>=0&&r<allRequests.size()) supporter.completeRequest(allRequests.get(r)); }
                case 6 -> allRequests.forEach(r->System.out.println("  #"+r.getRequestId()+" ["+r.getStatus()+"] "+r.getDescription()+" ("+r.getUrgency()+")"));
                case 0 -> back = true;
                default -> System.out.println(t("Invalid.","Неверный ввод.","Қате."));
            }
        }
    }

    static void printReqs() {
        for(int i=0;i<allRequests.size();i++){TechRequest r=allRequests.get(i);System.out.println("  "+(i+1)+". #"+r.getRequestId()+" ["+r.getStatus()+"] "+r.getDescription());}
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    static int readInt(String prompt) {
        while(true){System.out.print(prompt);try{return Integer.parseInt(scanner.nextLine().trim());}catch(NumberFormatException e){System.out.println(t("Enter a number.","Введите число.","Сан енгізіңіз."));}}
    }
    static double readDouble(String prompt) {
        while(true){System.out.print(prompt);try{return Double.parseDouble(scanner.nextLine().trim());}catch(NumberFormatException e){System.out.println(t("Enter a number.","Введите число.","Сан енгізіңіз."));}}
    }
    static String readString(String prompt) {
        System.out.print(prompt); return scanner.nextLine().trim();
    }
}