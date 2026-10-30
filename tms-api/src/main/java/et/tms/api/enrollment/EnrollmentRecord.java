package et.tms.api.enrollment;

import java.time.Instant;

public record EnrollmentRecord(
        String id,
        String studentId,
        String courseCode,
        Instant enrolledAt
){}