import java.util.Objects;


public class Course {
    private final String code;
    private String title;
    private int capacity;
    private int enrolledCount;


    public Course(String code, String title , int capacity){
        this.code = Objects.requireNonNull(code, "code");
        setTitle(title);
        setCapacity(capacity);
    }
    public String getCode(){ return code;}
    public String getTitle() {return title;}
    public int getCapacity() {return capacity;}
    public int getEnrolledCount() {return enrolledCount; }

    public void setTitle(String title ) {
        if(title == null || title.isBlank())
            throw new IllegalArgumentException("Title can not be empty ");
        this.title = title;
    }
    public void setCapacity(int capacity){
        if(capacity <= 0)
            throw new IllegalArgumentException("capacity can not be less that zero");
        this.capacity = capacity;
    }
    public void setEnrolledCount(int enrolledCount){
        if(enrolledCount < 0)
            throw new IllegalArgumentException(" Enrolled count cannot be negative");
        this.enrolledCount = enrolledCount;
    }

}