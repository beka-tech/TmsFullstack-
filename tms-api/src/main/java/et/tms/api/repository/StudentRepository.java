package et.tms.api.repository;
import et.tms.api.entity.Student;
import et.tms.api.report.StudentEnrollmentCount;
import et.tms.api.report.StudentSummary;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StudentRepository
        extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    long countByActiveTrueAndGpaGreaterThanEqual(BigDecimal minium);

    // StudentRepository: Approach A, a subquery
    @Query("select s.name from Student s where s.enrollments is empty")
    List<String> namesWithoutEnrollmentsSubquery();
    // StudentRepository: Approach B, a LEFT JOIN
    @Query("""
 select s.name from Student s
 left join s.enrollments e
 where e.id is null""")
    List<String> namesWithoutEnrollmentsLeftJoin();

    @Query("""
    SELECT new et.tms.api.report.StudentSummary(
        s.id,
        s.name,
        s.gpa
    )
    FROM Student s
    """)
    Slice<StudentSummary> findSummaries(Pageable pageable);


//    @EntityGraph(attributePaths = {"enrollments"})
//    @Query("SELECT DISTINCT s FROM Student s")
//    List<Student> findAllWithEnrollments();

    // StudentRepository: single query with projection
    @Query("""
 select new et.tms.api.report.StudentEnrollmentCount(s.name, count(e))
 from Student s left join s.enrollments e
 group by s.id, s.name""")
    List<StudentEnrollmentCount> enrollmentCounts();


    // Normal query: @SQLRestriction applies automatically
    // findAll(Pageable pageable) is already inherited.

    // Admin query: native SQL bypasses @SQLRestriction
    @Query(
            value = """
            SELECT *
            FROM students
            WHERE deleted = true
            ORDER BY id
            """,
            nativeQuery = true
    )
    List<Student> findDeletedForAdmin();

    // Restore a deleted student
    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE students
            SET deleted = false
            WHERE id = :id
            """,
            nativeQuery = true
    )
    int restore(@Param("id") long id);



}