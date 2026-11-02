package et.tms.api.enrollment;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class InMemoryEnrollmentService implements EnrollmentService {
    private static final Logger log =
            LoggerFactory.getLogger(InMemoryEnrollmentService.class);
    // Beans are singletons shared by every request thread: use a thread-safe map
    private final Map<String, EnrollmentRecord> store = new ConcurrentHashMap<>();

    @Override
    public EnrollmentRecord enroll(String studentId, String courseCode) {
        // Check for a duplicate enrollment
        // (check-then-act is not atomic; M5's unique constraint closes that gap)

        Optional<EnrollmentRecord> existing = store.values().stream()
                .filter(e -> e.studentId().equals(studentId)
                        && e.courseCode().equals(courseCode))
                .findFirst();
        if(existing.isPresent()){
            log.atWarn()
                    .setMessage("Duplicate enrollment attempt")
                    .addKeyValue("studentId", studentId)
                    .addKeyValue("courseCode",courseCode)
                    .addKeyValue("enrollmentId",existing.get().id())
                    .log();
            return existing.get();
    }

        String id = UUID.randomUUID().toString().substring(0, 8);
        var enrollment =
                new EnrollmentRecord(id, studentId, courseCode, Instant.now());
        store.put(id, enrollment);
        log.atInfo()
                .setMessage("Enrolled student")
                .addKeyValue("studentId", studentId)
                .addKeyValue("courseCode", courseCode)
                .addKeyValue("enrollmentId", id)
                .log();
        return enrollment;


    }
        @Override
    public List<EnrollmentRecord> findAll() {
        return List.copyOf(store.values());
    }
    @Override
    public Optional<EnrollmentRecord>findById(String id){
        Optional<EnrollmentRecord> found = Optional.ofNullable(store.get(id));

        if(found.isEmpty()){
            log.atWarn().setMessage("Enrollment not found")
                    .addKeyValue("enrollmentId", id).log();
        }

        return found;
    }
    @Override
    public boolean delete(String id){
        boolean removed = store.remove(id) !=null;
        if(removed) {
            log.atInfo().setMessage("Deleted enrollment")
                    .addKeyValue("enrollmentId", id).log();

        }else {
            log.atWarn().setMessage("Delete Failed: enrollment not found")
                    .addKeyValue("enrollmentId",id).log();
        }
        return removed;
    }

//
//    public EnrollmentRecord enroll(String studentId, String courseCode) {
//        String id = UUID.randomUUID().toString().substring(0, 8);
//        var enrollment =
//                new EnrollmentRecord(id, studentId, courseCode, Instant.now());
//        store.put(id, enrollment);
//        log.info("Enrolled {} in {} - record {}", studentId, courseCode, id);
//        return enrollment;
//    }
//    @Override
//    public Optional<EnrollmentRecord> findById(String id) {
//        return Optional.ofNullable(store.get(id));
//    }
//    @Override
//    public List<EnrollmentRecord> findAll() {
//        return List.copyOf(store.values());
//    }
//    @Override
//    public boolean delete(String id) {
//        return store.remove(id) != null;
//    }
}