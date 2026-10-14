import java.math.BigDecimal;
import java.time.Instant;

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

}

void printGradeReport(List<? extends Gradable> assessments){
    IO.println("---Grade Report ---");
    for (Gradable item : assessments){
        IO.println("%s: %.2f%%" .formatted(item.title(), item.calculateGrade()));
    }
}



