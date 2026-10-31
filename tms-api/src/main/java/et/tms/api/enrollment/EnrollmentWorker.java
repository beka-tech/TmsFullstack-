package et.tms.api.enrollment;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentWorker{

    private final EnrollmentBatch batch;

    public EnrollmentWorker(EnrollmentBatch batch){
        this.batch = batch;
    }

    public int processBatch (){
        batch.add("Scholarship-run" + Instant.now());
        return batch.size();
    }
}