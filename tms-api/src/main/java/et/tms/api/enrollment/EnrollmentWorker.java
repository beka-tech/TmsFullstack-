package et.tms.api.enrollment;
import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentWorker{

    private final ObjectProvider<EnrollmentBatch> batches;

    public EnrollmentWorker(ObjectProvider<EnrollmentBatch> batches){
        this.batches = batches;
    }

    public int  processBatch (){
        EnrollmentBatch batch = batches.getObject();
       int size = batch.size();
//       log.info("new EnrollmentBatch #{}",System.identityHashCode(this));
        return size;
    }
}