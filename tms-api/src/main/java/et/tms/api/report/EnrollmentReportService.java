package et.tms.api.report;

import et.tms.api.entity.Student;
import et.tms.api.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class EnrollmentReportService {
        private static final Logger log =
                LoggerFactory.getLogger(EnrollmentReportService.class);
        private final StudentRepository students;

        public EnrollmentReportService(StudentRepository students) {
            this.students = students;
        }
    @Transactional(readOnly = true)
    public void printCountsShaped() {
        for (StudentEnrollmentCount r : students.enrollmentCounts()) {
            log.info("{}: {} enrollments", r.name(), r.enrollmentCount());
        }
    }

}


