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

        // 1. Normalize sorting input
        // Convert client input to a consistent format for lookup.
        // Example: "TITLE" -> "title"
        String key = request.orderBy() == null
                ? ""
                : request.orderBy().toLowerCase(Locale.ROOT);

        // 2. Whitelist sortable properties
        // Allow only approved properties; otherwise default to "title".
        // Never trust raw sorting input from the client.
        String property = SORTABLE.getOrDefault(key, "title");

        // 3. Construct deterministic sorting
        // Choose ASC or DESC based on the client's request.
        // Use ID as a tie-breaker to ensure consistent ordering
        // when multiple records have the same property value.
        Sort.Direction direction = request.descending()
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(
                new Sort.Order(direction, property),
                Sort.Order.asc("id")
        );

        // 4. Construct Pageable
        // Convert API's one-based page to Spring's zero-based page.
        // Combine page number, page size, and sorting instructions.
        int zeroBasedPage = Math.max(0, request.page() - 1);

        Pageable pageable = PageRequest.of(
                zeroBasedPage,
                request.pageSize(),
                sort
        );

        // 5. Execute repository query
        // Let the database search, sort, paginate, and count
        // matching records instead of processing them in Java.
        Page<CourseResponse> page =
                courses.search(request.search(), pageable);

        // 6. Construct response
        // Return the current page's records, total matching records,
        // and pagination metadata to the client.
        return PagedResponse.of(
                page.getContent(),
                page.getTotalElements(),
                request.page(),
                request.pageSize()
        );
    }



}
