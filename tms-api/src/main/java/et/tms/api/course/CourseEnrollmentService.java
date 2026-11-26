package et.tms.api.course;
import et.tms.api.entity.Course;
import et.tms.api.entity.Enrollment;
import et.tms.api.entity.Student;
import et.tms.api.error.ApiProblem;
import et.tms.api.repository.CourseRepository;
import et.tms.api.repository.EnrollmentRepository;
import et.tms.api.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseEnrollmentService {
    private static final Logger log =
            LoggerFactory.getLogger(CourseEnrollmentService.class);
    private final CourseRepository courses;
    private final StudentRepository students;
    private final EnrollmentRepository enrollments;
    public CourseEnrollmentService(CourseRepository courses,
                                   StudentRepository students,
                                   EnrollmentRepository enrollments) {
        this.courses = courses;
        this.students = students;
        this.enrollments = enrollments;
    }
    @Transactional(readOnly = true)
    public EnrollmentResponse get(long courseId, long id) {
        return enrollments.findResponse(courseId, id).orElseThrow(() ->
                ApiProblem.notFound("Enrollment not found",
                        "Course %d has no enrollment %d."
                                .formatted(courseId, id)));
    }
    @Transactional
    public EnrollmentResponse enroll(long courseId, long studentId) {
        // TODO 2: apply these gates IN THIS ORDER, each with ApiProblem:
        // 1. courses.findById(courseId) is empty
        // -> 404 "Course not found"

        Course course = courses.findById(courseId)
                .orElseThrow(() -> ApiProblem.notFound(
                        "Course not found",
                        String.format("No course exists with id %s.", courseId)
                ));

        // 2. students.findById(studentId) is empty
        // -> 404 "Student not found"

        Student student = students.findById(studentId)
                .orElseThrow(() -> ApiProblem.notFound(
                        "Student not found", "Student %s not found".formatted(studentId)));


        // 3. enrollments.existsByCourseIdAndStudentId(...) is true
        // -> 409 "Student already enrolled"

        if(enrollments.existsByCourseIdAndStudentId(courseId, studentId)){
            throw ApiProblem.conflict("Already Enrolled", "Student is %s Already enrolled in %s Courses ".formatted(studentId, courseId) );
        }

        // 4. enrollments.countByCourseId(courseId)
        // >= course.getMaxCapacity()
        // -> 409 "Course is full"
        // detail: "Course '<title>' has reached its maximum
        // capacity of <n>."

        int enrolled = (int) enrollments.countByCourseId(courseId);

        if ( enrolled >= course.getMaxCapacity()) {
            throw ApiProblem.conflict(
                    "Full",
                    String.format(
                            "Course '%s' has reached its maximum capacity of %d.",
                            course.getTitle(),
                            course.getMaxCapacity()
                    )
            );
        }


        Enrollment saved = enrollments.saveAndFlush(
                new Enrollment(student, course, null)
        );

        log.info("Created enrollment id={} courseId={} studentId={}",
                saved.getId(), courseId, studentId);

        return new EnrollmentResponse(
                saved.getId(),
                courseId,
                studentId,
                saved.getEnrolledAt()
        );




//        throw new UnsupportedOperationException("TODO 2");
    }
}