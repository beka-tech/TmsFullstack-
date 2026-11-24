package et.tms.api.course;
import et.tms.api.entity.Course;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courses;
    public CourseController(CourseService courses) {
        this.courses = courses;
    }
    @GetMapping("/{id}")
    public Course get(@PathVariable Long id) {
        return courses.get(id);
    }

    @PostMapping
    public ResponseEntity<Course> create(
            @RequestBody CreateCourseRequest request) {

        Course created = courses.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
}
