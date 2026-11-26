package et.tms.api.course;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
@RestController
@RequestMapping("/api/courses/{courseId}/enrollments")
public class CourseEnrollmentController {
    private final CourseEnrollmentService enrollments;
    public CourseEnrollmentController(CourseEnrollmentService enrollments) {
        this.enrollments = enrollments;
    }
    @GetMapping("/{id}")
    public EnrollmentResponse get(@PathVariable Long courseId,
                                  @PathVariable Long id) {
        return enrollments.get(courseId, id);
    }
    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(
            @PathVariable Long courseId,
            @Valid @RequestBody EnrollStudentRequest request) {

        EnrollmentResponse response =
                enrollments.enroll(courseId, request.studentId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}