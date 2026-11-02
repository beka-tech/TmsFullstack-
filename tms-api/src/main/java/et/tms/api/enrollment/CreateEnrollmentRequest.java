package et.tms.api.enrollment;
import jakarta.validation.constraints.NotBlank;

public record CreateEnrollmentRequest(@NotBlank String studentId,
                                      @NotBlank String courseCode){}

