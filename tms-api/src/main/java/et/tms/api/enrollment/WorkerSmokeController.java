package et.tms.api.enrollment;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WorkerSmokeController {
    private final EnrollmentWorker worker;

    public WorkerSmokeController(EnrollmentWorker worker){
        this.worker = worker;
    }
    @GetMapping("api/enrollments/worker-smoke")
    public Map<String, Integer>smoke(){
        return Map.of("processed", worker.processBatch());
    }
}
