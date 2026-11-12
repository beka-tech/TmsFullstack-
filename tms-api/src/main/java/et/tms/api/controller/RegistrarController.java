package et.tms.api.controller;

import et.tms.api.report.CourseAverageGpa;
import et.tms.api.report.CourseEnrollmentCount;
import et.tms.api.report.StudentSummary;
import et.tms.api.repository.CourseRepository;
import et.tms.api.repository.EnrollmentRepository;
import et.tms.api.repository.StudentRepository;


import java.math.BigDecimal;
import java.util.List;

import et.tms.api.service.RegistrarService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/registrar")
public class RegistrarController {

    private final StudentRepository students;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;
    private final RegistrarService registra;

    public RegistrarController(StudentRepository students,
                               CourseRepository courses,
                               EnrollmentRepository enrollments, RegistrarService registra) {
        this.students = students;
        this.courses = courses;
        this.enrollments = enrollments;
        this.registra = registra;
    }

    @GetMapping("/students/active-count")
    public long activeStudentCount(@RequestParam(defaultValue = "3.0") BigDecimal minimumGpa) {
        return students.countByActiveTrueAndGpaGreaterThanEqual(minimumGpa);
    }

    @GetMapping("/courses/enrollment-counts")
    public List<CourseEnrollmentCount> enrollmentCounts() {
        return courses.coursesByEnrollmentCount();
    }

    @GetMapping("/courses/average-gpa")
    public List<CourseAverageGpa> averageGpa() {
        return enrollments.averageGpaPerCourse();
    }

    @GetMapping("/students/without-enrollments/subquery")
    public List<String> withoutEnrollmentsSubquery() {
        return students.namesWithoutEnrollmentsSubquery();
    }

    @GetMapping("/students/without-enrollments/left-join")
    public List<String> withoutEnrollmentsLeftJoin() {
        return students.namesWithoutEnrollmentsLeftJoin();
    }

    @GetMapping("/students")
    public Slice<StudentSummary> getStudents(
            @RequestParam(defaultValue = "1") int page
    ) {
        if (page < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page must be at least 1"
            );
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                20,
                Sort.by("name", "id")
        );

        return students.findSummaries(pageable);
    }

    @GetMapping("/courses/top")
    public List<CourseEnrollmentCount> getTopCourses() {
        return registra.getTopCourses();
    }
}

