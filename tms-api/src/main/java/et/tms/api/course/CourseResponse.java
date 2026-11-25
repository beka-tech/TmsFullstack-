package et.tms.api.course;

public record CourseResponse(
        Long id,
        String code,
        String title,
        int maxCapacity,
        long enrollmentCount
) {
    /*
   True While a seat is free;
   Drives the conditional enroll links.
   * */
    public boolean hasRoom(){
        return enrollmentCount < maxCapacity;
    }
}
