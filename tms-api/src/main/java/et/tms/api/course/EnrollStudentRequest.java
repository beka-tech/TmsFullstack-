package et.tms.api.course;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnrollStudentRequest(

        @NotNull
        @Positive
        Long studentId
) {
}
