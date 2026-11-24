package et.tms.api.course;

import et.tms.api.entity.Course;
import et.tms.api.error.ApiProblem;
import et.tms.api.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class CourseService {

    private static final Logger log =
            LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courses;
    public CourseService(CourseRepository courses) {
        this.courses = courses;
    }
    @Transactional(readOnly = true)
    public Course get(long id) {
        // TODO 1: return courses.findById(id), or throw
        // ApiProblem.notFound("Course not found",
        // "No course with id <id>.")
        // (you create ApiProblem in Step 2).
//        throw new UnsupportedOperationException("TODO 1");
        return courses.findById( id).orElseThrow(() -> ApiProblem.notFound(
                "Course Not Found",
                "Course with Id " +id + "does not exit"
        ));
    }
    @Transactional
    public Course create(CreateCourseRequest request) {
        Course course = new Course(
                request.code(),
                request.title(),
                request.maxCapacity()
        );

        Course saved = courses.save(course);



        log.info("Created course id={} code={}",
                saved.getId(), request.code());

        return saved;
    }

}
