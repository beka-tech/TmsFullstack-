package et.tms.api.service;

import et.tms.api.report.StudentSummary;
import et.tms.api.entity.Student;
import et.tms.api.repository.StudentRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository students;

    public StudentService(StudentRepository students) {
        this.students = students;
    }

    @Transactional(readOnly = true)
    public Page<StudentSummary> getStudents(int page) {

        // 1. Define pagination
        Pageable pageable = PageRequest.of(
                page - 1,
                20,
                Sort.by("name", "id")
        );

        // 2. Retrieve only the requested page
        Page<Student> result = students.findAll(pageable);

        // 3. Convert entities into DTOs
        return result.map(student ->
                new StudentSummary(
                        student.getId(),
                        student.getName(),
                        student.getGpa()
                )
        );
    }
}