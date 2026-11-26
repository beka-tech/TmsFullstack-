package et.tms.api.course;

import java.time.Instant;

public record EnrollmentResponse(
        Long id,
        Long courseId,
        Long studentId,
        Instant enrolledAt
) {
}
