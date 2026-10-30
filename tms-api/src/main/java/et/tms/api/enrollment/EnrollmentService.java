package et.tms.api.enrollment;
import java.util.List;
import java.util.Optional;
// --- The contract ---
public interface EnrollmentService {
    EnrollmentRecord enroll(String studentId, String courseCode);
    Optional<EnrollmentRecord> findById(String id);
    List<EnrollmentRecord> findAll();
    boolean delete(String id);
}