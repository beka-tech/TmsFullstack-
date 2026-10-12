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
//    IO.println(course.setTitle("ad"));


  }



