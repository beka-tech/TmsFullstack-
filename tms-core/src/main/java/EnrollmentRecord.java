import java.time.Instant;

public record EnrollmentRecord(String studentId,
                               String courseCode,
                               Instant enrolledAt){
    public EnrollmentRecord withCourseCode(String newCourseCode){
        return new EnrollmentRecord(studentId, newCourseCode,enrolledAt);
    }
}