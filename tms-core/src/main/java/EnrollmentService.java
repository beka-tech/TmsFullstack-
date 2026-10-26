import java.time.Instant;
import java.util.*;

public class EnrollmentService {
    public EnrollmentRecord processRegistration(Student student, Course course){

        // TODO 1: Guard clauses — fail fast

        Objects.requireNonNull(student, "student");
        Objects.requireNonNull(course, "course");

        if(course.getEnrolledCount() >= course.getCapacity())
//            throw new IllegalStateException("Course '%s' is at Full capacity ".formatted(course.getTitle()));
            throw new CapacityReachedException(course.getCode());


        // TODO 2: Classify academic standing with guarded patterns

        String standing = switch (student) {
            case Student s when s.gpaAtLeast("3.5")
                    -> "Honors";

            case Student s when s.gpaAtLeast("2.5")
                    -> "Good Standing";

            case Student s
                    -> "Academic Warning";
        };

        IO.println(student.name() + "is in " + standing +". ");

        // TODO 3: Build the enrollment record

        return new EnrollmentRecord (student.id(),course.getCode(), Instant.now()) ;
    }
}
