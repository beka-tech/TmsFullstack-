package et.tms.api.repository;

import et.tms.api.entity.Course;
import et.tms.api.report.CourseEnrollmentCount;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CourseRepository
        extends JpaRepository<Course, Long> {

    @Query("""
        SELECT new et.tms.api.report.CourseEnrollmentCount(
            c.title,
            COUNT(e)
        )
        FROM Enrollment e
        JOIN e.course c
        GROUP BY c.id, c.title
        ORDER BY COUNT(e) DESC, c.title ASC, c.id ASC
        """)
    List<CourseEnrollmentCount> coursesByEnrollmentCount(
            Pageable pageable
    );
}
