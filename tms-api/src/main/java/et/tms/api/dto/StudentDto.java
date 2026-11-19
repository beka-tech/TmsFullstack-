package et.tms.api.dto;

import et.tms.api.entity.Student;
import java.math.BigDecimal;

public record StudentDto(
        Long id,
        String name,
        BigDecimal gpa,
        long version
) {

    public static StudentDto from(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getGpa(),
                student.getVersion()
        );
    }
}