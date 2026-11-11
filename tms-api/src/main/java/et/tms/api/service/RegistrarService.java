package et.tms.api.service;

import et.tms.api.report.CourseEnrollmentCount;
import et.tms.api.repository.CourseRepository;
import et.tms.api.repository.EnrollmentRepository;
import et.tms.api.repository.StudentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class RegistrarService {
    private final StudentRepository students;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;

    public RegistrarService(StudentRepository students, CourseRepository courses, EnrollmentRepository enrollments) {
        this.students = students;
        this.courses = courses;
        this.enrollments = enrollments;
    }
    public List<CourseEnrollmentCount> getTopCourses() {

        Pageable topFive = PageRequest.of(0, 5);

        return courses.coursesByEnrollmentCount(
                topFive
        );
    }
}
