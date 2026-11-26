package et.tms.api.repository;

import et.tms.api.course.CourseResponse;
import et.tms.api.entity.Course;
import et.tms.api.report.CourseEnrollmentCount;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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
            );


    boolean existsByCode(String code); // used in Exercise 3

    @Query("""
        select new et.tms.api.course.CourseResponse(
            c.id, c.code, c.title, c.maxCapacity, count(e))
        from Course c left join c.enrollments e
        where c.id = :id
        group by c.id, c.code, c.title, c.maxCapacity""")
    Optional<CourseResponse> findResponseById(@Param("id") Long id);

}
