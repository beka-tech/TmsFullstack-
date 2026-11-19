package et.tms.api.service;

import et.tms.api.dto.StudentDto;
import et.tms.api.dto.UpdateStudentRequest;
import et.tms.api.exception.StudentNotFoundException;
import et.tms.api.report.StudentSummary;
import et.tms.api.entity.Student;
import et.tms.api.repository.StudentRepository;

import org.springframework.data.domain.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    @Transactional(readOnly = true)
    public StudentDto findById(long id){

        Student student  = students.findById(id)
                .orElseThrow(
                () -> new StudentNotFoundException(id)
        );
        return StudentDto.from(student);
    }

    @Transactional
    public StudentDto update(long id, UpdateStudentRequest request) {

        Student student = students.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (!Objects.equals(student.getVersion(), request.version())) {
            throw new ObjectOptimisticLockingFailureException(
                    Student.class,
                    id
            );
        }

        student.setName(request.name());
        student.setGpa(request.gpa());

        Student saved = students.saveAndFlush(student);

        return StudentDto.from(saved);
    }

}