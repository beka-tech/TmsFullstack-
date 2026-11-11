package et.tms.api.repository;
import et.tms.api.entity.Enrollment;
import et.tms.api.report.CourseAverageGpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long >, JpaSpecificationExecutor<Enrollment> {
    @Query(
            """
                    select new et.tms.api.report.CourseAverageGpa(c.title, avg(s.gpa))
                     from Enrollment e join e.course c join e.student s
                     group by c.title
                    
                    """
    )
    List<CourseAverageGpa> averageGpaPerCourse();
        }


