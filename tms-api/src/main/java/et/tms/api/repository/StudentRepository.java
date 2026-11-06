package et.tms.api.repository;
import et.tms.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface StudentRepository
        extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
}