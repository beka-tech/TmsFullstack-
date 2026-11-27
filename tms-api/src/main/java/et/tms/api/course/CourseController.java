package et.tms.api.course;
import et.tms.api.common.PagedRequest;
import et.tms.api.common.PagedResponse;
import et.tms.api.entity.Course;
import java.net.URI;

import jakarta.validation.Valid;
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

    @GetMapping
    public PagedResponse<CourseResponse> list(PagedRequest request) {
        return courses.search(request);
    }


    @GetMapping("/{id}")
    public CourseResponse get(@PathVariable Long id) {
        return courses.get(id);
    }



    @PostMapping
    public ResponseEntity<CourseResponse> create(
            @Valid @RequestBody CreateCourseRequest request) {
        CourseResponse created = courses.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }
}
