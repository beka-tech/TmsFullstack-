import java.math.BigDecimal;
import java.time.Instant;

import java.util.List;
//import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.*;


void main() {


    var enrollment = new EnrollmentRecord("STU-001", "CS-401",Instant.now());

    var duplicate = new EnrollmentRecord("STU-001", "CS-401",enrollment.enrolledAt());

    IO.println("Same Data "+ enrollment.equals(duplicate));
    IO.println("Same Object " + (enrollment == duplicate));

    IO.println(enrollment);

    var course = new Course("CS-401","Advance Java",30);

    course.setTitle("Java");
//    course.setCapacity(-4);

    try {
        course.setCapacity(-5);
    }
    catch (IllegalArgumentException ex){
        IO.println("Caught:" + ex.getMessage());
    }

    try {
        course.setTitle("");
    }
    catch (IllegalArgumentException ex){
        IO.println("Caught:" +ex.getMessage());
    }

    IO.println("Course: %s (Capacity %d)".formatted(course.getTitle(),course.getCapacity() ));

    IO.println(course.getCapacity());

    var abebe = Student.of("S1", "Abebe",20,"3.8");
    IO.println("Student Id %s, name %s Age %d GPA %s".formatted(abebe.id(), abebe.name(), abebe.age(), abebe.gpa() ) );

    // Student.of("S2", "", 20, "3.0");
    var abebe2 = Student.of("S3", "Test", 22, "3.0");
    IO.println("Student Id %s, name %s Age %d GPA %s".formatted(abebe2.id(), abebe2.name(), abebe2.age(), abebe2.gpa() ) );

// Student.of("S3", "Test", 12, "3.0");
// Student.of("S4", "Test", 20, "5.0");
List <Gradable > cohortAssessments = List.of(
        new Quiz("Java Basics", 18,20),
        new LabAssignment("Registration API",new BigDecimal("90"),new BigDecimal("85")));

printGradeReport(cohortAssessments);

// uard Clauses, Pattern Matching, Collections and Streams
    var service = new EnrollmentService();
// Test 1: Valid registration
    var validStudent = Student.of("S1", "Abeba", 20, "3.8");
    var validCourse = new Course("CS-401", "Advanced Java", 30);
    var result = service.processRegistration(validStudent, validCourse);
    IO.println("Enrolled: %s in %s".formatted(result.studentId(), result.courseCode()));
// Test 2: Null student should throw
    try {
        service.processRegistration(null, validCourse);
    } catch (NullPointerException ex) {
        IO.println("Guard caught: " + ex.getMessage());
    }
// Test 3: Full course should throw
    var fullCourse = new Course("CS-402", "Full Course", 1);
    fullCourse.setEnrolledCount(1);
    try {
        service.processRegistration(validStudent, fullCourse);
    } catch (IllegalStateException ex) {
        IO.println("Business rule: " + ex.getMessage());
    }

    List<Student> students = List.of(
            Student.of("S1", "Abeba", 22, "3.8"),
            Student.of("S2", "Kidane", 21, "2.4"),
            Student.of("S3", "Dawit", 20, "3.1"),
            Student.of("S4", "Sara", 23, "3.9"),
            Student.of("S5", "Frehiwot", 19, "2.0"),
            Student.of("S6", "Yonas", 24, "3.5"),
            Student.of("S7", "Meron", 22, "1.8"),
            Student.of("S8", "Tesfaye", 21, "2.9"));

    List<Student> leaderboard = students.stream()
            .filter( student ->student.gpaAtLeast("3.5"))
            .sorted(Comparator.comparing(Student::gpa).reversed())
//            .map(student -> Student(student.name())) how to get the name without changing the <Student> to <String>
//            .map(student -> new Student(student.getName()))

            .toList();


    IO.println("Found %d Honors Students:".formatted(leaderboard.size()));
    for (Student name : leaderboard) {
        IO.println("- " + name);
    }

    // TODO 5: Add up all GPAs, then divide by the number of students,
    // keeping 2 decimal places.

    BigDecimal averageGpa = students.stream()
            .map(Student::gpa)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(students.size()), 2, RoundingMode.HALF_UP);


    IO.println("\n Class Average GPA:" + averageGpa);


    Map<String, List<Student>> standingGroups = students.stream()
            .collect(Collectors.groupingBy(s -> standing(s),
                    LinkedHashMap::new, Collectors.toList()));


    IO.println("\n ---Academic Standing Report ----");
    standingGroups.forEach((label, group)->{
        IO.println("\n%s (%d):".formatted(label, group.size()));
        group.forEach(s->IO.println("%s GPA: %s". formatted(s.name(), s.gpa())));
    });

    List<String> backendCourses = List.of("Java", "Spring Boot");
    List<String> frontendCourses = List.of("TypeScript","Angular");
    List<String> allCourses = Stream.of(backendCourses, frontendCourses, List.of("Capstone")).flatMap(List::stream).toList();

    IO.println("\nFull curriculum: " + String.join(", ",allCourses));

    // the wrong way platform threads
    long start = System.nanoTime();
    for(int i = 0; i < 5; i++){
        simulateDbCall(300);
    }
    IO.println("Blocking sequential: %d ms " .formatted(elapsedMs(start)));


    // STILL STARVED: concurrent, but only 2 platform threads for 5 Database calls
    start = System.nanoTime();
    try (ExecutorService pool = Executors.newFixedThreadPool(2)){
        for(int i=0;i<5;i++){
            pool.submit(()->simulateDbCall(300));
        }
    }
    IO.println("small thread pool: %d ms" .formatted(elapsedMs(start)) );


    // THE RIGHT WAY one Virtual thread per call, all 5 wait at the same time

    start = System.nanoTime();
    try (ExecutorService vts = Executors.newVirtualThreadPerTaskExecutor() ){
        for (int i = 0; i < 5; i++){
            vts.submit(()->simulateDbCall(300));
        }
    }
    IO.println("Virtual threads: %d ms".formatted(elapsedMs(start)));

    long runStart = System.nanoTime();
    List<String> studentIds = List.of("S1","S2","S3","S4","S5");
    List<String> courseCodes = List.of("CRS-101","CRS-201","CRS-301");

    List<Student> loadedStudents;
    List<Course> loadedCourses ;

    try (ExecutorService io = Executors.newVirtualThreadPerTaskExecutor()){
        // START every fetch (student and Course ) and Keep the future in a list
        List<CompletableFuture<Student>> studentFutures = studentIds.stream()
//                .map(id-> supplyAsync(() -> fetchCourse(id),io)
                .map(id->CompletableFuture.supplyAsync(() -> fetchStudent(id), io))
                .toList();


        List<CompletableFuture<Course>> courseFutures = courseCodes.stream()
                .map(code -> CompletableFuture.supplyAsync(() -> fetchCourse(code), io))
                .toList();

                loadedStudents = studentFutures.stream().map(CompletableFuture::join).toList();
                loadedCourses = courseFutures.stream().map(CompletableFuture::join).toList();
    }

    IO.println("\nLoaded %d students and %d courses in %d ms"
            .formatted(loadedStudents.size(), loadedCourses.size(), elapsedMs(runStart)));
    for (Student s : loadedStudents) {
        IO.println(" %s GPA: %s %s" .formatted(s.name(), s.gpa(),Thread.currentThread().getName() ));
    }

    var enrollCourse = new Course("CRS-101","Java Mastery",2);
    var enrollService = new EnrollmentService();

    List<EnrollmentRecord> enrollments = new ArrayList<>();
    List<String> failures = new ArrayList<>();

    for(Student student : loadedStudents){
        try {
            EnrollmentRecord enrolled =
                    enrollService.processRegistration(student,enrollCourse);
            enrollCourse.setEnrolledCount(enrollCourse.getEnrolledCount() + 1);
            enrollments.add(enrolled);
            IO.println("Enrolled: " + student.name());
        } catch (IllegalStateException ex){
//        } catch (CapacityReachedException ex){
            failures.add(student.name() + ": " + ex.getMessage());
            IO.println("Rejected: "+ student.name() + " - "+ex.getMessage());
        }
    }

//    Thread.startVirtualThread(()->sendConfirmation());

//    Student Name = ;?


    // fire and forget
//    for (Student s : loadedStudents) {
//        Thread.startVirtualThread(() -> sendConfirmation(s));
//
//    }

    try {
        var overflowCourse = new Course("CRS-999","Overflow Test",1);
        overflowCourse.setEnrolledCount(1);
        enrollService.processRegistration(Student.of("S99","Test",20,"3.0"),overflowCourse);
    }
    catch (CapacityReachedException ex ) {
        IO.println("\nDomain exception caught :");
        IO.println(" Course : "+ ex.getCourseCode());
        IO.println(" Message: "+ ex.getMessage());

    }




}
//----------------------------------------------
String standing(Student s) {
    return switch (s) {
        case Student x when x.gpaAtLeast("3.5") -> "Honors";
        case Student x when x.gpaAtLeast("2.5") -> "Good Standing";
        case Student x when x.gpaAtLeast("2.0") -> "Probation";
        case Student x -> "Academic Warning";
    };
}

void printGradeReport(List<? extends Gradable> assessments){
    IO.println("---Grade Report ---");
    for (Gradable item : assessments){
        IO.println("%s: %.2f%%" .formatted(item.title(), item.calculateGrade()));
    }
}

void simulateDbCall(long millis){
    try {
        Thread.sleep(millis);
    }
    catch (InterruptedException e){
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Interrupted while waiting", e);
    }
}

long elapsedMs(long startNanos) {
    return (System.nanoTime() - startNanos) / 1_000_000;
}


Student fetchStudent(String id){
    IO.println(" Fetching " + id + "...");
    simulateDbCall(300);
    String gpa = switch(id){
        case "S1" ->"3.8";
        case "S2" ->"2.4";
        case "S3" ->"3.5";
        case "S4" ->"1.9";
        case "S5" ->"3.2";
        default -> "2.5";
    };
    return Student.of(id, "Student" + id, 20,gpa);

}

Course fetchCourse(String code){
    IO.println(" Fecth course " + code + "....");
    simulateDbCall(200);
    int capacity = switch (code){
        case "CRS-101"->2;
        case "CRS-201"->30;
        case "CRS-301"->15;
        default -> 25;
    };
    return new Course(code, "Course- " + code, capacity);
}

//void sendConfirmation(Student student ){
//try {
//    simulateDbCall(100);
//    IO.println("Email sent to " + student.name());
//}
//    catch (RuntimeException ex){
//    IO.println("Email failed for " + student.name()+ " : " + ex.getMessage() );
//    }
//}