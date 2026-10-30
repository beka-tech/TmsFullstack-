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
        String id = UUID.randomUUID().toString().substring(0, 8);
        var enrollment =
                new EnrollmentRecord(id, studentId, courseCode, Instant.now());
        store.put(id, enrollment);
        log.info("Enrolled {} in {} - record {}", studentId, courseCode, id);
        return enrollment;
    }
    @Override
    public Optional<EnrollmentRecord> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
    @Override
    public List<EnrollmentRecord> findAll() {
        return List.copyOf(store.values());
    }
    @Override
    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}