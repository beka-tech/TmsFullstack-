import java.math.BigDecimal;
import java.util.Objects;

public record Student(String id, String name, int age, BigDecimal gpa ){

    private static final BigDecimal MAX_GPA = new BigDecimal("4.0");

    public Student {
        Objects.requireNonNull(id, "id");
        if(name == null || name.isBlank())
            throw new IllegalArgumentException(
                    "Name cannot be empty or whitespace. ");

        if(age < 16 || age > 80)
            throw new IllegalArgumentException("Age Must be between 16 and 80. ");

        Objects.requireNonNull(gpa, "gpa");
        if(gpa.signum() <0 || gpa.compareTo(MAX_GPA) > 0)
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");

//        if(gpa.)

    }

}