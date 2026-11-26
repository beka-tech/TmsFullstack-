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
    public CourseResponse get(long id) {
        return courses.findResponseById( id).orElseThrow(() -> ApiProblem.notFound(
                "Course Not Found",
                "Course with Id " +id + "does not exit"
        ));
    }


    @Transactional
    public CourseResponse create(CreateCourseRequest request) {

        if(courses.existsByCode(request.code())){
            throw ApiProblem.conflict("Courses code Already Exists",
                                         "A Courses With '%s' is already registered ".formatted(request.code()) );
        }

        Course saved = courses.saveAndFlush(new Course(
                request.code(), request.title(), request.maxCapacity()));
        log.info("Created course {} ({})", saved.getId(), saved.getCode());
        return get(saved.getId());
    }



}
