package et.tms.api.assessment;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {
    @GetMapping("/results")
    public Map<String, String> results() {
// Placeholder body: real persistence and DTOs arrive in later modules
        return Map.of("courseCode", "CS-101",
                "studentId", "S-001",
                "letterGrade", "A");
    }
}