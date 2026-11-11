package et.tms.api.repository;
import et.tms.api.entity.Student;
import et.tms.api.report.CourseEnrollmentCount;
import et.tms.api.report.StudentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

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




}