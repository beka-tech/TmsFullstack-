package et.tms.api.controller;

import et.tms.api.dto.StudentDto;
import et.tms.api.dto.UpdateStudentRequest;
import et.tms.api.report.StudentSummary;
import et.tms.api.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;
    private final StudentService students;

    public StudentController(StudentService service, StudentService students) {
        this.service = service;
        this.students = students;
    }

    @GetMapping
    public Page<StudentSummary> getStudents(
            @RequestParam(defaultValue = "1") int page
    ) {
        if (page < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page must be at least 1"
            );
        }

        return service.getStudents(page);
    }

    @GetMapping ("/{id}")
    public StudentDto getStudent(@PathVariable long id){
        return students.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        students.softdelete(id);
    }

    @PutMapping("/{id}")
        public StudentDto updateStudent(
                @PathVariable  long id,
                @Valid @RequestBody UpdateStudentRequest request
        ) {
        return students.update(id, request);
    }
}