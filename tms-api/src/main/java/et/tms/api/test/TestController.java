package et.tms.api.test;
import et.tms.api.entity.Student;
import et.tms.api.repository.StudentRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final StudentRepository students;
    public TestController(StudentRepository students) {
        this.students = students;
    }
    @GetMapping("/deferred")
    public List<String> deferred() {
        log.info(">>> STEP 1: Building the specification (no database contact)...");
        Specification<Student> gpaAtLeast3 = (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("gpa"), new BigDecimal("3.0"));
        log.info(">>> STEP 2: Adding a second condition...");
        Specification<Student> recipe =
                gpaAtLeast3.and((root, query, cb) -> cb.isTrue(root.get("active")));
        log.info(">>> STEP 3: Executing the query...");
        List<Student> results = students.findAll(recipe, Sort.by("name")); // SQL!
        log.info(">>> STEP 4: Finished: {} rows.", results.size());
        return results.stream().map(Student::getName).toList(); // names only
    }
    // Plain Java: Hibernate cannot see inside this method
    private static boolean isHonorRoll(BigDecimal gpa) {
        return gpa.compareTo(new BigDecimal("3.5")) >= 0;
    }
    @GetMapping("/in-memory")
    public List<String> inMemory() {
        log.info(">>> Loading every student, then filtering in Java...");
        return students.findAll().stream() // SELECT ... FROM students: ALL rows
                .filter(s -> isHonorRoll(s.getGpa()))
                .map(Student::getName)
                .toList();
    }
}