package et.tms.api.course;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Z]{2,4}-\\d{3}$",
                message = "Code must look like CS-101 "
                        + "(2-4 capitals, hyphen, 3 digits)")
        String code,
        @NotBlank
        @Size(max = 200)
        String title,
        @Min(value = 1, message = "Capacity must be 1 to 200 students")
        @Max(value = 200, message = "Capacity must be 1 to 200 students")
        int maxCapacity) {}
