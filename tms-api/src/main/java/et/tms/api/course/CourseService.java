package et.tms.api.course;

import et.tms.api.common.PagedRequest;
import et.tms.api.common.PagedResponse;
import et.tms.api.entity.Course;
import et.tms.api.error.ApiProblem;
import et.tms.api.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

@Service

public class CourseService {

    private static final Logger log =
            LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courses;

    // Whitelist: client text never reaches the query as a property name.
    private static final Map<String, String> SORTABLE = Map.of(
            "title", "title", "code", "code",
            "maxcapacity", "maxCapacity");

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

    @Transactional(readOnly = true)
    public PagedResponse<CourseResponse> search(PagedRequest request) {
        // TODO 1: Pick the sort property. Look request.orderBy() up,
        // lower-cased, in SORTABLE; an unknown value falls back to "title".
        // Never pass the raw string on.
        // Stuck?
        // String property = SORTABLE.getOrDefault(
        // request.orderBy().toLowerCase(Locale.ROOT), "title");
        String property = SORTABLE.getOrDefault(request.orderBy().toLowerCase(Locale.ROOT), "title");

        // TODO 2: Build the Sort: the property in the requested direction,
        // then Sort.Order.asc("id") as a tie-breaker.
        // Stuck?
        // Sort.Direction direction = request.descending()
        // ? Sort.Direction.DESC : Sort.Direction.ASC;
        // Sort sort = Sort.by(new Sort.Order(direction, property),
        // Sort.Order.asc("id"));
        Sort.Direction direction = request.descending() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(new Sort.Order(direction, property),Sort.Order.asc("id"));

        // TODO 3: Build the Pageable. Spring pages are ZERO-based; the API's
        // pages are one-based, so subtract one.
        // PageRequest.of(request.page() - 1, request.pageSize(), sort)

        int zeroBasedPage = Math.max(0, request.page() - 1);

        Pageable pageable = PageRequest.of(
                zeroBasedPage,
                request.pageSize(),
                sort
        );

        // TODO 4: Call courses.search(request.search(), pageable).

        Page<CourseResponse> page = courses.search(request.search(),pageable);

        // TODO 5: Return PagedResponse.of(...) with the page content, the
        // TOTAL from the Page object (getTotalElements), and the page and
        // pageSize from the request.
        return PagedResponse.of(
                page.getContent(),
                page.getTotalElements(),
                request.page(),
                request.pageSize());
    }



}
