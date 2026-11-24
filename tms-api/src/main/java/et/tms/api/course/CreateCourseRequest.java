package et.tms.api.course;

public record CreateCourseRequest(
//        Long id,
        String code,
        String title,
        int maxCapacity
) {
}
