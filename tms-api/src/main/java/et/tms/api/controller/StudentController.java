package et.tms.api.controller;

import et.tms.api.report.StudentSummary;
import et.tms.api.service.StudentService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
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
}