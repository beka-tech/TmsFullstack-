package et.tms.api.dev;
import et.tms.api.entity.*;
import et.tms.api.repository.*;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@Profile("!prod") // never seeds production
public class DevDataSeeder implements ApplicationRunner {
    private final StudentRepository students;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;
    public DevDataSeeder(StudentRepository students, CourseRepository courses,
                         EnrollmentRepository enrollments) {
        this.students = students;
        this.courses = courses;
        this.enrollments = enrollments;
    }
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (students.count() > 0) return; // seed once
        var abeba = new Student("TMS-2026-0001", "Abeba Tesfaye", gpa("3.80"));
        var bekele = new Student("TMS-2026-0002", "Bekele Alemu", gpa("2.90"));
        var chaltu = new Student("TMS-2026-0003", "Chaltu Gemechu", gpa("3.40"));
        chaltu.setActive(false);
        var dawit = new Student("TMS-2026-0004", "Dawit Haile", gpa("3.90"));
        var eleni = new Student("TMS-2026-0005", "Eleni Kebede", gpa("2.50"));
        students.saveAll(List.of(abeba, bekele, chaltu, dawit, eleni));
        var cs101 = new Course("CS-101", "Introduction to Computer Science", 30);
        var cs201 = new Course("CS-201", "Data Structures and Algorithms", 25);
        var mat101 = new Course("MAT-101", "Calculus I", 40);
        courses.saveAll(List.of(cs101, cs201, mat101));
        enrollments.saveAll(List.of(
                new Enrollment(abeba, cs101, new BigDecimal("4.00")),
                new Enrollment(abeba, cs201, new BigDecimal("3.60")),
                new Enrollment(bekele, cs101, new BigDecimal("2.80")),
                new Enrollment(dawit, cs201, new BigDecimal("3.90"))));
    }
    private static BigDecimal gpa(String value) {
        return new BigDecimal(value);
    }
}