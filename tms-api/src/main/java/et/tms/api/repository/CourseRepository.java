package et.tms.api.repository;

import et.tms.api.course.CourseResponse;
import et.tms.api.entity.Course;
import et.tms.api.report.CourseEnrollmentCount;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Provides CRUD operations and custom course queries.
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Counts enrollments per course, ordered by popularity.
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
    List<CourseEnrollmentCount> coursesByEnrollmentCount();


    // Checks whether a course code already exists.
    boolean existsByCode(String code);


    // Retrieves a course DTO with its enrollment count.
    @Query("""
        SELECT new et.tms.api.course.CourseResponse(
            c.id,
            c.code,
            c.title,
            c.maxCapacity,
            COUNT(e)
        )
        FROM Course c
        LEFT JOIN c.enrollments e
        WHERE c.id = :id
        GROUP BY c.id, c.code, c.title, c.maxCapacity
        """)
    Optional<CourseResponse> findResponseById(@Param("id") Long id);


    // Case-insensitive search by title or code with pagination.
    // countQuery calculates total matching courses.
    @Query(
            value = """
            SELECT new et.tms.api.course.CourseResponse(
                c.id,
                c.code,
                c.title,
                c.maxCapacity,
                COUNT(e)
            )
            FROM Course c
            LEFT JOIN c.enrollments e
            WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%'))
            GROUP BY c.id, c.code, c.title, c.maxCapacity
            """,

            countQuery = """
            SELECT COUNT(c)
            FROM Course c
            WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%'))
            """
    )
    Page<CourseResponse> search(
            @Param("search") String search,
            Pageable pageable
    );
}