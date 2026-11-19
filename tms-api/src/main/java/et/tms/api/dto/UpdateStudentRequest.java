package et.tms.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateStudentRequest(

        @NotBlank
        String name,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("4.0")
        BigDecimal gpa,

        @NotNull
        Long version
) {}