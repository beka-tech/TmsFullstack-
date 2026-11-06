package et.tms.api.error;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class DiagnosticsController {
    @GetMapping("/api/error")
    public String fail() {
        throw new TmsDatabaseException(
                "Simulated database failure for ProblemDetail testing");
    }
}