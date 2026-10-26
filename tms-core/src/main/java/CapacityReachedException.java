public class CapacityReachedException extends IllegalStateException {

    private final String courseCode;

    public CapacityReachedException(String courseCode){
        super("Course" + courseCode + "has reached maximum capacity");
        this.courseCode = courseCode;
    }

    public CapacityReachedException(String courseCode, Throwable cause){
        super("Course" + courseCode + "has reached maximum capacity, ", cause );
        this.courseCode = courseCode;
    }

    public String getCourseCode() {return courseCode; }
}
