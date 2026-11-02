package et.tms.api.enrollment;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController (EnrollmentService enrollmentService ){
        this.enrollmentService = enrollmentService;
    }

    // GET /api/enrollments: returns all enrollment records
    @GetMapping
    public List<EnrollmentRecord> getAll(){
        return enrollmentService.findAll();
    }

    @PostMapping
    public ResponseEntity<EnrollmentRecord>create(
            @Valid
            @RequestBody CreateEnrollmentRequest request
    ){
        EnrollmentRecord created =
                enrollmentService.enroll(request.studentId(), request.courseCode());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return enrollmentService.delete(id)
                ?ResponseEntity.noContent().build()
                :ResponseEntity.notFound().build();
    }

}