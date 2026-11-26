package et.tms.api.repository;
import et.tms.api.course.EnrollmentResponse;
import et.tms.api.entity.Enrollment;
import et.tms.api.report.CourseAverageGpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
    long countByCourseId(Long courseId);

    @Query ("""
            SELECT new et.tms.api.course.EnrollmentResponse( e.id, e.course.id,e.student.id,e.enrolledAt)
                                    From Enrollment e
                                                WHERE e.course.id =:courseId and e.id =:id
            """)
    Optional<EnrollmentResponse> findResponse(
            @Param("courseId") Long courseId,
            @Param("id") Long id );

}


